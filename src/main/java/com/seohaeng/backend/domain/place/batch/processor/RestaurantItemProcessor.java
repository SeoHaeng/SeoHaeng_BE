package com.seohaeng.backend.domain.place.batch.processor;

import com.seohaeng.backend.domain.place.batch.dto.RestaurantSyncResult;
import com.seohaeng.backend.domain.place.batch.exception.TourApiRetryableException;
import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.*;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.RestaurantAttributeRepository;
import com.seohaeng.backend.domain.place.batch.TourApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class RestaurantItemProcessor implements ItemProcessor<AreaBasedSearchResponseDTO.PlaceItem, RestaurantSyncResult> {

    private final TourApiClient tourApiClient;
    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final RestaurantAttributeRepository restaurantAttributeRepository;

    @Override
    public RestaurantSyncResult process(AreaBasedSearchResponseDTO.PlaceItem item) throws Exception {
        Place place = findOrCreatePlace(item.getContentid());
        log.info("[Item Start] 장소 명 : " + item.getTitle() + " / contentId = " + item.getContentid());

        if (place.getModifiedtime().equals(item.getModifiedtime())) {
            log.info("변경일 같음, 스킵");
            return null;
        }

        updateBasicInfo(place, item);
        List<PlaceImage> images = processImages(place, item);

        RestaurantAttribute attr = findOrCreateAttribute(place);

        DetailIntroResponse introResponse = tourApiClient.getDetailIntroInfo(39, item.getContentid());
        DetailCommonResponseDTO commonResponse = tourApiClient.getDetailCommonInfo(item.getContentid());

        if (introResponse == null || commonResponse == null) {
            throw new TourApiRetryableException("상세 API 응답 없음: contentId=" + item.getContentid());
        }

        if (introResponse instanceof DetailIntroRestaurantResponseDTO restaurantDto) {
            var introItems = restaurantDto.getResponse().getBody().getItems();
            var commonItems = commonResponse.getResponse().getBody().getItems();
            if (introItems == null || introItems.getItem() == null || introItems.getItem().isEmpty()
                    || commonItems == null || commonItems.getItem() == null || commonItems.getItem().isEmpty()) {
                throw new TourApiRetryableException("상세 API items 없음: contentId=" + item.getContentid());
            }

            DetailIntroRestaurantResponseDTO.RestaurantIntroItem intro = introItems.getItem().get(0);
            var commonItem = commonItems.getItem().get(0);

            if (!Objects.equals(attr.getFirstmenu(), intro.getFirstmenu()))
                attr.setFirstmenu(intro.getFirstmenu());
            if (!Objects.equals(attr.getTreatmenu(), intro.getTreatmenu()))
                attr.setTreatmenu(intro.getTreatmenu());
            if (!Objects.equals(attr.getKidsfacility(), intro.getKidsfacility()))
                attr.setKidsfacility(intro.getKidsfacility());
            if (!Objects.equals(attr.getIsSmokingAllowed(), intro.getSmoking()))
                attr.setIsSmokingAllowed(intro.getSmoking());
            if (!Objects.equals(attr.getIsTakeoutAvailable(), intro.getPacking()))
                attr.setIsTakeoutAvailable(intro.getPacking());
            if (!Objects.equals(attr.getHasParking(), intro.getParkingfood()))
                attr.setHasParking(intro.getParkingfood());
            if (!Objects.equals(attr.getIsReservable(), intro.getReservationfood()))
                attr.setIsReservable(intro.getReservationfood());

            if (!Objects.equals(place.getUseTime(), intro.getOpentimefood()))
                place.setUseTime(intro.getOpentimefood());
            if (!Objects.equals(place.getTel(), item.getTel()))
                place.setTel(item.getTel());
            if (!Objects.equals(place.getWebsiteUrl(), commonItem.getHomepage()))
                place.setWebsiteUrl(commonItem.getHomepage());
        }

        place.setModifiedtime(item.getModifiedtime());
        log.info("[Processor] 음식점 처리 완료: {}", place.getName());
        return new RestaurantSyncResult(place, attr, images);
    }

    private Place findOrCreatePlace(String contentId) {
        return placeRepository.findByContentId(contentId).orElseGet(() ->
                Place.builder()
                        .contentId(contentId)
                        .placeType(PlaceType.RESTAURANT)
                        .name("")
                        .modifiedtime("")
                        .build());
    }

    private RestaurantAttribute findOrCreateAttribute(Place place) {
        if (place.getId() == null)
            return RestaurantAttribute.builder().place(place).build();
        return restaurantAttributeRepository.findByPlace(place)
                .orElseGet(() -> RestaurantAttribute.builder().place(place).build());
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
