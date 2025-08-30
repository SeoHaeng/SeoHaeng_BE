package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.*;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.RestaurantAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.TouristSpotAttributeRepository;
import com.seohaeng.backend.domain.place.util.TourApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineService {

    private final TourApiClient tourApiClient;
    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;

    private final FestivalAttributeRepository festivalAttributeRepository;
    private final RestaurantAttributeRepository restaurantAttributeRepository;
    private final TouristSpotAttributeRepository touristSpotAttributeRepository;

    // 관광지
    public void getAllTourSpotDatas() {
        log.info("관광지 데이터 파이프라인 시작");
        
        List<AreaBasedSearchResponseDTO.PlaceItem> allPlaceItems = new java.util.ArrayList<>();
        
        for (int pageNo = 1; pageNo <= 1; pageNo++) {
            AreaBasedSearchResponseDTO tourSpotDataList = tourApiClient.searchAreaBasedPlaces(12, pageNo);
            if (tourSpotDataList == null ||
                tourSpotDataList.getResponse().getBody().getItems() == null ||
                tourSpotDataList.getResponse().getBody().getItems().getItem() == null ||
                tourSpotDataList.getResponse().getBody().getItems().getItem().isEmpty()) {
                break;
            }
            List<AreaBasedSearchResponseDTO.PlaceItem> currentPageItems 
                    = tourSpotDataList.getResponse().getBody().getItems().getItem();
            allPlaceItems.addAll(currentPageItems);
        }

        List<AreaBasedSearchResponseDTO.PlaceItem> placeItems = allPlaceItems;

        for (AreaBasedSearchResponseDTO.PlaceItem placeItem : placeItems) {
            try {
                processSingleTouristSpot(placeItem);
            } catch (Exception e) {
                log.error("관광지 처리 실패: {}", placeItem.getTitle(), e);
            }
        }
        log.info("관광지 데이터 파이프라인 완료");
    }

    @Transactional
    public void processSingleTouristSpot(AreaBasedSearchResponseDTO.PlaceItem placeItem) {
        log.info("=== 관광지 처리 시작 ===");
        log.info("API 데이터 - Title: {}, ContentId: {}, ModifiedTime: {}",
            placeItem.getTitle(), placeItem.getContentid(), placeItem.getModifiedtime());

        Place place = checkDuplicatePlace(placeItem.getContentid(), PlaceType.TOURIST_SPOT);

        log.info("DB 데이터 - Name: {}, ModifiedTime: {}", place.getName(), place.getModifiedtime());

        if (!place.getModifiedtime().equals(placeItem.getModifiedtime())) { // 수정일이 다른 경우 -> 수정
            log.info("수정일이 다름 - 업데이트 로직 시작");

            // Place 기본 정보 업데이트
            updatePlaceBasicInfo(place, placeItem);

            // Image
            processPlaceImages(place, placeItem);

            DetailIntroResponse detailIntroResponse = tourApiClient.getDetailIntroInfo(12, placeItem.getContentid()); // 소개정보
            DetailCommonResponseDTO detailCommonResponseDTO = tourApiClient.getDetailCommonInfo(placeItem.getContentid()); // 공통정보

            TouristSpotAttribute touristSpotAttribute = checkDuplicateTouristSpotAttribute(place);

            if (detailIntroResponse instanceof DetailIntroTouristResponseDTO touristDto) {
                log.info("DetailIntroTouristResponseDTO 타입 확인 성공");

                DetailIntroTouristResponseDTO.TouristIntroItem touristIntroItem
                        = touristDto.getResponse().getBody().getItems().getItem().get(0);

                if(!Objects.equals(touristSpotAttribute.getParkingAvailable(), touristIntroItem.getParking())){
                    touristSpotAttribute.setParkingAvailable(touristIntroItem.getParking());
                }
                if(!Objects.equals(touristSpotAttribute.getPetsAllowed(), touristIntroItem.getChkpet())){
                    touristSpotAttribute.setPetsAllowed(touristIntroItem.getChkpet());
                }
                if(!Objects.equals(touristSpotAttribute.getBabyCarriageAllowed(), touristIntroItem.getChkbabycarriage())){
                    touristSpotAttribute.setBabyCarriageAllowed(touristIntroItem.getChkbabycarriage());
                }
                if(!Objects.equals(touristSpotAttribute.getCreditCardAccepted(), touristIntroItem.getChkcreditcard())){
                    touristSpotAttribute.setCreditCardAccepted(touristIntroItem.getChkcreditcard());
                }

                String newOverview = detailCommonResponseDTO.getResponse().getBody().getItems().getItem().get(0).getOverview();
                if(!Objects.equals(touristSpotAttribute.getOverview(), newOverview)){
                    touristSpotAttribute.setOverview(newOverview);
                }

                if(!Objects.equals(place.getUseTime(), touristIntroItem.getUsetime())){
                    place.setUseTime(touristIntroItem.getUsetime());
                }
                if(!Objects.equals(place.getTel(), placeItem.getTel())){
                    place.setTel(placeItem.getTel());
                }
                String newWebsiteUrl = detailCommonResponseDTO.getResponse().getBody().getItems().getItem().get(0).getHomepage();
                if(!Objects.equals(place.getWebsiteUrl(), newWebsiteUrl)){
                    place.setWebsiteUrl(newWebsiteUrl);
                }

                touristSpotAttributeRepository.save(touristSpotAttribute);
            }
            place.setModifiedtime(placeItem.getModifiedtime());
            placeRepository.save(place);
            log.info("Place 정보 저장 완료");
        } else {
            log.info("수정일이 같음 - 업데이트 스킵");
        }
    }

    // TouristSpotAttribute DB 중복체크
    private TouristSpotAttribute checkDuplicateTouristSpotAttribute(Place place){
        Optional<TouristSpotAttribute> touristSpotAttribute = touristSpotAttributeRepository.findByPlace(place);
        if (touristSpotAttribute.isPresent()) {
            return touristSpotAttribute.get();
        } else {
            return touristSpotAttributeRepository.save(
                    TouristSpotAttribute.builder()
                            .place(place)
                            .build());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 음식점
    public void getAllRestaurantDatas() {
        log.info("음식점 데이터 파이프라인 시작");
        
        List<AreaBasedSearchResponseDTO.PlaceItem> allPlaceItems = new java.util.ArrayList<>();
        
        for (int pageNo = 1; pageNo <= 1; pageNo++) {
            AreaBasedSearchResponseDTO tourSpotDataList = tourApiClient.searchAreaBasedPlaces(39, pageNo);
            if (tourSpotDataList == null ||
                tourSpotDataList.getResponse().getBody().getItems() == null ||
                tourSpotDataList.getResponse().getBody().getItems().getItem() == null ||
                tourSpotDataList.getResponse().getBody().getItems().getItem().isEmpty()) {
                break;
            }
            List<AreaBasedSearchResponseDTO.PlaceItem> currentPageItems 
                    = tourSpotDataList.getResponse().getBody().getItems().getItem();
            allPlaceItems.addAll(currentPageItems);
        }

        List<AreaBasedSearchResponseDTO.PlaceItem> placeItems = allPlaceItems;

        for (AreaBasedSearchResponseDTO.PlaceItem placeItem : placeItems) {
            try {
                processSingleRestaurant(placeItem);
            } catch (Exception e) {
                log.error("음식점 처리 실패: {}", placeItem.getTitle(), e);
            }
        }
        log.info("음식점 데이터 파이프라인 완료");
    }

    @Transactional
    public void processSingleRestaurant(AreaBasedSearchResponseDTO.PlaceItem placeItem) {
        Place place = checkDuplicatePlace(placeItem.getContentid(), PlaceType.RESTAURANT);

        if (!place.getModifiedtime().equals(placeItem.getModifiedtime())) {
            // Place 기본 정보 업데이트
            updatePlaceBasicInfo(place, placeItem);

            // Image
            processPlaceImages(place, placeItem);

            // Attribute
            RestaurantAttribute restaurantAttribute = checkDuplicateRestaurantAttribute(place);

            DetailIntroResponse detailIntroResponse = tourApiClient.getDetailIntroInfo(39, placeItem.getContentid()); // 소개정보
            if (detailIntroResponse instanceof DetailIntroRestaurantResponseDTO restaurantDto) {

                DetailIntroRestaurantResponseDTO.RestaurantIntroItem restaurantIntroItem
                        = restaurantDto.getResponse().getBody().getItems().getItem().get(0);

                if(!Objects.equals(restaurantAttribute.getFirstmenu(), restaurantIntroItem.getFirstmenu())) {
                    restaurantAttribute.setFirstmenu(restaurantIntroItem.getFirstmenu());
                }
                if(!Objects.equals(restaurantAttribute.getTreatmenu(), restaurantIntroItem.getTreatmenu())) {
                    restaurantAttribute.setTreatmenu(restaurantIntroItem.getTreatmenu());
                }
                if(!Objects.equals(restaurantAttribute.getKidsfacility(), restaurantIntroItem.getKidsfacility())){
                    restaurantAttribute.setKidsfacility(restaurantIntroItem.getKidsfacility());
                }
                if(!Objects.equals(restaurantAttribute.getIsSmokingAllowed(), restaurantIntroItem.getSmoking())){
                    restaurantAttribute.setIsSmokingAllowed(restaurantIntroItem.getSmoking());
                }
                if(!Objects.equals(restaurantAttribute.getIsTakeoutAvailable(), restaurantIntroItem.getPacking())){
                    restaurantAttribute.setIsTakeoutAvailable(restaurantIntroItem.getPacking());
                }
                if(!Objects.equals(restaurantAttribute.getHasParking(), restaurantIntroItem.getParkingfood())){
                    restaurantAttribute.setHasParking(restaurantIntroItem.getParkingfood());
                }
                if(!Objects.equals(restaurantAttribute.getIsReservable(), restaurantIntroItem.getReservationfood())){
                    restaurantAttribute.setIsReservable(restaurantIntroItem.getReservationfood());
                }

                DetailCommonResponseDTO detailCommonResponseDTO = tourApiClient.getDetailCommonInfo(placeItem.getContentid()); // 공통정보
                if(!Objects.equals(place.getUseTime(), restaurantIntroItem.getOpentimefood())){
                    place.setUseTime(restaurantIntroItem.getOpentimefood());
                } // 영업시간
                if(!Objects.equals(place.getTel(), placeItem.getTel())){
                    place.setTel(placeItem.getTel());
                } // 전화번호
                String newWebsiteUrl = detailCommonResponseDTO.getResponse().getBody().getItems().getItem().get(0).getHomepage();
                if(!Objects.equals(place.getWebsiteUrl(), newWebsiteUrl)){
                    place.setWebsiteUrl(newWebsiteUrl);
                } // 웹사이트
            }
            restaurantAttributeRepository.save(restaurantAttribute);

            place.setModifiedtime(placeItem.getModifiedtime());
            placeRepository.save(place);
            log.info("Place 정보 저장 완료");
        }else {
            log.info("수정일이 같음 - 업데이트 스킵");
        }
    }

    // RestaurantAttribute DB 중복체크
    private RestaurantAttribute checkDuplicateRestaurantAttribute(Place place){
        Optional<RestaurantAttribute> restaurantAttribute = restaurantAttributeRepository.findByPlace(place);
        if (restaurantAttribute.isPresent()) {
            return restaurantAttribute.get();
        } else {
            return restaurantAttributeRepository.save(
                    RestaurantAttribute.builder()
                            .place(place)
                            .build());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 축제
    public void getAllFestivalDatas() {
        log.info("축제 데이터 파이프라인 시작");
        
        List<AreaBasedSearchResponseDTO.PlaceItem> allPlaceItems = new java.util.ArrayList<>();
        
        for (int pageNo = 1; pageNo <= 1; pageNo++) {
            AreaBasedSearchResponseDTO tourSpotDataList = tourApiClient.searchAreaBasedPlaces(15, pageNo);
            if (tourSpotDataList == null ||
                tourSpotDataList.getResponse().getBody().getItems() == null ||
                tourSpotDataList.getResponse().getBody().getItems().getItem() == null ||
                tourSpotDataList.getResponse().getBody().getItems().getItem().isEmpty()) {
                break;
            }
            List<AreaBasedSearchResponseDTO.PlaceItem> currentPageItems 
                    = tourSpotDataList.getResponse().getBody().getItems().getItem();
            allPlaceItems.addAll(currentPageItems);
        }

        List<AreaBasedSearchResponseDTO.PlaceItem> placeItems = allPlaceItems;

        for (AreaBasedSearchResponseDTO.PlaceItem placeItem : placeItems) {
            try {
                processSingleFestival(placeItem);
            } catch (Exception e) {
                log.error("축제 처리 실패: {}", placeItem.getTitle(), e);
            }
        }
        log.info("축제 데이터 파이프라인 완료");
    }

    @Transactional
    public void processSingleFestival(AreaBasedSearchResponseDTO.PlaceItem placeItem) {
        Place place = checkDuplicatePlace(placeItem.getContentid(), PlaceType.FESTIVAL);
        if (!place.getModifiedtime().equals(placeItem.getModifiedtime())) { // 수정일이 다른 경우 -> 수정

            // Place 기본 정보 업데이트
            updatePlaceBasicInfo(place, placeItem);

            // Image
            processPlaceImages(place, placeItem);

            // Attribute
            FestivalAttribute festivalAttribute = checkDuplicateFestivalAttribute(place);

            DetailCommonResponseDTO detailCommonResponseDTO = tourApiClient.getDetailCommonInfo(placeItem.getContentid()); // 공통정보
            String newOverview = detailCommonResponseDTO.getResponse().getBody().getItems().getItem().get(0).getOverview();

            DetailIntroResponse detailIntroResponse = tourApiClient.getDetailIntroInfo(15, placeItem.getContentid()); // 소개정보
            if (detailIntroResponse instanceof DetailIntroFestivalResponseDTO festivalResponseDTO) {

                DetailIntroFestivalResponseDTO.FestivalIntroItem festivalIntroItem
                        = festivalResponseDTO.getResponse().getBody().getItems().getItem().get(0);

                if(!Objects.equals(place.getUseTime(), festivalIntroItem.getPlaytime())){
                    place.setUseTime(festivalIntroItem.getPlaytime());
                }
                if(!Objects.equals(place.getTel(), placeItem.getTel())){
                    place.setTel(placeItem.getTel());
                }
                String newWebsiteUrl = detailCommonResponseDTO.getResponse().getBody().getItems().getItem().get(0).getHomepage();
                if(!Objects.equals(place.getWebsiteUrl(), newWebsiteUrl)){
                    place.setWebsiteUrl(newWebsiteUrl);
                }

                if(!Objects.equals(festivalAttribute.getOverview(), newOverview)) {
                    festivalAttribute.setOverview(newOverview);
                }

                LocalDate startDate = parseDate(festivalIntroItem.getEventstartdate());
                if(!Objects.equals(festivalAttribute.getStartDate(), startDate)){
                    festivalAttribute.setStartDate(startDate);
                }

                LocalDate endDate = parseDate(festivalIntroItem.getEventenddate());
                if(!Objects.equals(festivalAttribute.getEndDate(), endDate)){
                    festivalAttribute.setEndDate(endDate);
                }

                DetailRepeatResponseDTO detailRepeatResponseDTO = tourApiClient.getDetailRepeatInfo(15, placeItem.getContentid());
                if(!Objects.equals(festivalAttribute.getPrograms(),
                        detailRepeatResponseDTO.getResponse().getBody().getItems().getItem().get(1).getInfotext())) {
                    festivalAttribute.setPrograms(detailRepeatResponseDTO.getResponse().getBody().getItems().getItem().get(1).getInfotext());
                }
            }

            festivalAttributeRepository.save(festivalAttribute);
            place.setModifiedtime(placeItem.getModifiedtime());
            placeRepository.save(place);
            log.info("Place 정보 저장 완료");
        }else {
            log.info("수정일이 같음 - 업데이트 스킵");
        }
    }

    // FestivalAttribute DB 중복체크
    private FestivalAttribute checkDuplicateFestivalAttribute(Place place){
        Optional<FestivalAttribute> festivalAttribute = festivalAttributeRepository.findByPlace(place);
        if (festivalAttribute.isPresent()) {
            return festivalAttribute.get();
        } else {
            return festivalAttributeRepository.save(
                    FestivalAttribute.builder()
                            .place(place)
                            .build());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Place DB 중복체크
    private Place checkDuplicatePlace (String contentId, PlaceType placeType){
        Optional<Place> place = placeRepository.findByContentId(contentId);
        if (place.isPresent()) {
            return place.get();
        } else {
            return placeRepository.save(
                    Place.builder()
                            .contentId(contentId)
                            .placeType(placeType)
                            .name("")
                            .modifiedtime("")
                            .build());
        }
    }

    // Place 기본 정보 업데이트 공통 메서드
    private void updatePlaceBasicInfo(Place place, AreaBasedSearchResponseDTO.PlaceItem placeItem) {
        // 장소명 업데이트
        if (!Objects.equals(place.getName(), placeItem.getTitle())) {
            place.setName(placeItem.getTitle());
        }
        
        // 주소 업데이트
        String newAddress = (placeItem.getAddr1() != null ? placeItem.getAddr1() : "") + " " + 
                           (placeItem.getAddr2() != null ? placeItem.getAddr2() : "");
        if (!Objects.equals(place.getAddress(), newAddress.trim())) {
            place.setAddress(newAddress.trim());
        }
        
        // 위경도 업데이트
        if (placeItem.getMapx() != null && placeItem.getMapy() != null &&
                (place.getLatitude() == null || place.getLongitude() == null ||
                        !Objects.equals(place.getLatitude().toString(), placeItem.getMapy())
                        || !Objects.equals(place.getLongitude().toString(), placeItem.getMapx()))) {
            place.setLatitude(Double.valueOf(placeItem.getMapy()));
            place.setLongitude(Double.valueOf(placeItem.getMapx()));
        }
    }

    // 이미지 처리 공통 메서드
    private void processPlaceImages(Place place, AreaBasedSearchResponseDTO.PlaceItem placeItem) {
        List<PlaceImage> existingImages = placeImageRepository.findByPlace(place);
        if (existingImages.isEmpty()) {
            if (placeItem.getFirstimage() != null && !placeItem.getFirstimage().isEmpty()) {
                PlaceImage firstImage = PlaceImage.builder()
                        .place(place)
                        .imageUrl(placeItem.getFirstimage())
                        .build();
                placeImageRepository.save(firstImage);
            }
            if (placeItem.getFirstimage2() != null && !placeItem.getFirstimage2().isEmpty()) {
                PlaceImage secondImage = PlaceImage.builder()
                        .place(place)
                        .imageUrl(placeItem.getFirstimage2())
                        .build();
                placeImageRepository.save(secondImage);
            }
        }
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            log.warn("날짜 파싱 실패: {}", dateString, e);
            return null;
        }
    }
}
