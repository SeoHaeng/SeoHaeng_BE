package com.seohaeng.backend.domain.place.batch.processor;

import com.seohaeng.backend.domain.place.batch.dto.TouristSpotSyncResult;
import com.seohaeng.backend.domain.place.batch.exception.TourApiRetryableException;
import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.*;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.TouristSpotAttributeRepository;
import com.seohaeng.backend.domain.place.batch.TourApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class TouristSpotItemProcessor implements ItemProcessor<AreaBasedSearchResponseDTO.PlaceItem, TouristSpotSyncResult> {

    private final TourApiClient tourApiClient;
    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final TouristSpotAttributeRepository touristSpotAttributeRepository;

    @Override
    public TouristSpotSyncResult process(AreaBasedSearchResponseDTO.PlaceItem item) throws Exception {
        Place place = findOrCreatePlace(item.getContentid());

        if (place.getModifiedtime().equals(item.getModifiedtime())) {
            return null;
        }

        updateBasicInfo(place, item);
        List<PlaceImage> images = processImages(place, item);

        DetailIntroResponse introResponse = tourApiClient.getDetailIntroInfo(12, item.getContentid());
        DetailCommonResponseDTO commonResponse = tourApiClient.getDetailCommonInfo(item.getContentid());

        if (introResponse == null || commonResponse == null) {
            throw new TourApiRetryableException("상세 API 응답 없음: contentId=" + item.getContentid());
        }

        TouristSpotAttribute attr = findOrCreateAttribute(place);

        if (introResponse instanceof DetailIntroTouristResponseDTO touristDto) {
            var introItems = touristDto.getResponse().getBody().getItems();
            var commonItems = commonResponse.getResponse().getBody().getItems();
            if (introItems == null || introItems.getItem() == null || introItems.getItem().isEmpty()
                    || commonItems == null || commonItems.getItem() == null || commonItems.getItem().isEmpty()) {
                throw new TourApiRetryableException("상세 API items 없음: contentId=" + item.getContentid());
            }

            DetailIntroTouristResponseDTO.TouristIntroItem intro = introItems.getItem().get(0);
            var commonItem = commonItems.getItem().get(0);

            if (!Objects.equals(attr.getParkingAvailable(), intro.getParking()))
                attr.setParkingAvailable(intro.getParking());
            if (!Objects.equals(attr.getPetsAllowed(), intro.getChkpet()))
                attr.setPetsAllowed(intro.getChkpet());
            if (!Objects.equals(attr.getBabyCarriageAllowed(), intro.getChkbabycarriage()))
                attr.setBabyCarriageAllowed(intro.getChkbabycarriage());
            if (!Objects.equals(attr.getCreditCardAccepted(), intro.getChkcreditcard()))
                attr.setCreditCardAccepted(intro.getChkcreditcard());
            if (!Objects.equals(place.getUseTime(), intro.getUsetime()))
                place.setUseTime(intro.getUsetime());
            if (!Objects.equals(place.getTel(), item.getTel()))
                place.setTel(item.getTel());

            if (!Objects.equals(attr.getOverview(), commonItem.getOverview()))
                attr.setOverview(commonItem.getOverview());
            if (!Objects.equals(place.getWebsiteUrl(), commonItem.getHomepage()))
                place.setWebsiteUrl(commonItem.getHomepage());
        }

        place.setModifiedtime(item.getModifiedtime());
        log.info("[Processor] 관광지 처리 완료: {}", place.getName());
        return new TouristSpotSyncResult(place, attr, images);
    }

    private Place findOrCreatePlace(String contentId) {
        return placeRepository.findByContentId(contentId).orElseGet(() ->
                Place.builder()
                        .contentId(contentId)
                        .placeType(PlaceType.TOURIST_SPOT)
                        .name("")
                        .modifiedtime("")
                        .build());
    }

    private TouristSpotAttribute findOrCreateAttribute(Place place) {
        if (place.getId() == null)
            return TouristSpotAttribute.builder().place(place).build();
        return touristSpotAttributeRepository.findByPlace(place)
                .orElseGet(() -> TouristSpotAttribute.builder().place(place).build());
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
}
