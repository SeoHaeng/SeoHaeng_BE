package com.seohaeng.backend.domain.place.batch.processor;

import com.seohaeng.backend.domain.place.batch.dto.FestivalSyncResult;
import com.seohaeng.backend.domain.place.batch.exception.TourApiRetryableException;
import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.*;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import com.seohaeng.backend.domain.place.batch.TourApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class FestivalItemProcessor implements ItemProcessor<AreaBasedSearchResponseDTO.PlaceItem, FestivalSyncResult> {

    private final TourApiClient tourApiClient;
    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final FestivalAttributeRepository festivalAttributeRepository;

    @Override
    public FestivalSyncResult process(AreaBasedSearchResponseDTO.PlaceItem item) throws Exception {
        Place place = findOrCreatePlace(item.getContentid());
        log.info("[Item Start] 장소 명 : " + item.getTitle() + " / contentId = " + item.getContentid());

        if (place.getModifiedtime().equals(item.getModifiedtime())) {
            log.info("변경일 같음, 스킵");
            return null;
        }

        updateBasicInfo(place, item);
        List<PlaceImage> images = processImages(place, item);

        FestivalAttribute attr = findOrCreateAttribute(place);

        DetailCommonResponseDTO commonResponse = tourApiClient.getDetailCommonInfo(item.getContentid());
        DetailIntroResponse introResponse = tourApiClient.getDetailIntroInfo(15, item.getContentid());

        if (commonResponse == null || introResponse == null) {
            throw new TourApiRetryableException("상세 API 응답 없음: contentId=" + item.getContentid());
        }

        if (introResponse instanceof DetailIntroFestivalResponseDTO festivalDto) {
            var introItems = festivalDto.getResponse().getBody().getItems();
            var commonItems = commonResponse.getResponse().getBody().getItems();
            if (introItems == null || introItems.getItem() == null || introItems.getItem().isEmpty()
                    || commonItems == null || commonItems.getItem() == null || commonItems.getItem().isEmpty()) {
                throw new TourApiRetryableException("상세 API items 없음: contentId=" + item.getContentid());
            }

            DetailIntroFestivalResponseDTO.FestivalIntroItem intro = introItems.getItem().get(0);
            var commonItem = commonItems.getItem().get(0);

            if (!Objects.equals(place.getUseTime(), intro.getPlaytime()))
                place.setUseTime(intro.getPlaytime());
            if (!Objects.equals(place.getTel(), item.getTel()))
                place.setTel(item.getTel());
            if (!Objects.equals(place.getWebsiteUrl(), commonItem.getHomepage()))
                place.setWebsiteUrl(commonItem.getHomepage());
            if (!Objects.equals(attr.getOverview(), commonItem.getOverview()))
                attr.setOverview(commonItem.getOverview());

            LocalDate startDate = parseDate(intro.getEventstartdate());
            if (!Objects.equals(attr.getStartDate(), startDate))
                attr.setStartDate(startDate);

            LocalDate endDate = parseDate(intro.getEventenddate());
            if (!Objects.equals(attr.getEndDate(), endDate))
                attr.setEndDate(endDate);

            DetailRepeatResponseDTO repeatResponse = tourApiClient.getDetailRepeatInfo(15, item.getContentid());
            if (repeatResponse != null) {
                var repeatItems = repeatResponse.getResponse().getBody().getItems();
                if (repeatItems != null && repeatItems.getItem() != null && repeatItems.getItem().size() > 1) {
                    String programs = repeatItems.getItem().get(1).getInfotext();
                    if (!Objects.equals(attr.getPrograms(), programs))
                        attr.setPrograms(programs);
                }
            }
        }

        place.setModifiedtime(item.getModifiedtime());
        log.info("[Processor] 축제 처리 완료: {}", place.getName());
        return new FestivalSyncResult(place, attr, images);
    }

    private Place findOrCreatePlace(String contentId) {
        return placeRepository.findByContentId(contentId).orElseGet(() ->
                Place.builder()
                        .contentId(contentId)
                        .placeType(PlaceType.FESTIVAL)
                        .name("")
                        .modifiedtime("")
                        .build());
    }

    private FestivalAttribute findOrCreateAttribute(Place place) {
        if (place.getId() == null)
            return FestivalAttribute.builder().place(place).build();
        return festivalAttributeRepository.findByPlace(place)
                .orElseGet(() -> FestivalAttribute.builder().place(place).build());
    }

    private void updateBasicInfo(Place place, AreaBasedSearchResponseDTO.PlaceItem item) {
        if (!Objects.equals(place.getName(), item.getTitle()))
            place.setName(item.getTitle());

        String address = ((item.getAddr1() != null ? item.getAddr1() : "") + " " +
                (item.getAddr2() != null ? item.getAddr2() : "")).trim();
        if (!Objects.equals(place.getAddress(), address))
            place.setAddress(address);

        if (item.getMapx() != null && item.getMapy() != null) {
            Double lat = Double.valueOf(item.getMapx());
            Double lon = Double.valueOf(item.getMapy());
            if (!Objects.equals(place.getLatitude(), lat)) place.setLatitude(lat);
            if (!Objects.equals(place.getLongitude(), lon)) place.setLongitude(lon);
        }
    }

    private List<PlaceImage> processImages(Place place, AreaBasedSearchResponseDTO.PlaceItem item) {
        if (place.getId() != null && !placeImageRepository.findByPlace(place).isEmpty()) return List.of();

        List<PlaceImage> images = new ArrayList<>();
        if (item.getFirstimage() != null && !item.getFirstimage().isEmpty())
            images.add(PlaceImage.builder().place(place).imageUrl(item.getFirstimage()).build());
        if (item.getFirstimage2() != null && !item.getFirstimage2().isEmpty())
            images.add(PlaceImage.builder().place(place).imageUrl(item.getFirstimage2()).build());
        return images;
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            log.warn("날짜 파싱 실패: {}", dateString);
            return null;
        }
    }
}