package com.seohaeng.backend.domain.place.converter;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;
import org.springframework.data.domain.Page;

import java.util.List;

public class PlaceConverter {

    public static PlaceResponseDTO.placeDto toplaceDto (Place place) {
     return PlaceResponseDTO.placeDto.builder()
             .id(place.getId())
             .name(place.getName())
             .placeType(place.getPlaceType())
             .address(place.getAddress())
             .region(place.getRegion().getRegionName())
             .latitude(place.getLatitude())
             .longitude(place.getLongitude())
             .build();
    }

    public static PlaceResponseDTO.BookStoreDto toBookStoreDto (Place place) {
        return PlaceResponseDTO.BookStoreDto.builder()
                .placeId(place.getId())
                .region(place.getRegion().getRegionName())
                .name(place.getName())
                .placeType(place.getPlaceType())
                .address(place.getAddress())
                .longitude(place.getLongitude())
                .latitude(place.getLatitude())
                .imageUrl(place.getPlaceImages() != null && !place.getPlaceImages().isEmpty() 
                    ? place.getPlaceImages().get(0).getImageUrl() : "https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/places/default.png")
                .build();
    }

    public static PlaceResponseDTO.BookStoreListDto toBookStoreListDto (
            Page<?> page,
            List<PlaceResponseDTO.BookStoreDto> placeDtoList) {

        return PlaceResponseDTO.BookStoreListDto.builder()
                .listSize(placeDtoList.size())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .placeList(placeDtoList)
                .build();
    }

    public static PlaceResponseDTO.TodayPlaceResponse toTodayPlaceResponse (Place place, String overview) {
        return PlaceResponseDTO.TodayPlaceResponse.builder()
                .placeId(place.getId())
                .name(place.getName())
                .overview(overview)
                .imageUrl(place.getPlaceImages() != null && !place.getPlaceImages().isEmpty() 
                    ? place.getPlaceImages().get(0).getImageUrl() : "https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/places/default.png")
                .placeType(place.getPlaceType())
                .build();
    }

    public static PlaceResponseDTO.OngoingFestivalResponse toOngoingFestivalResponse(Place place, FestivalAttribute attribute) {
        return PlaceResponseDTO.OngoingFestivalResponse.builder()
                .placeId(place.getId())
                .placeType(place.getPlaceType())
                .festivalName(place.getName())
                .startDate(attribute.getStartDate())
                .endDate(attribute.getEndDate())
                .imageUrl(place.getPlaceImages() != null && !place.getPlaceImages().isEmpty() 
                    ? place.getPlaceImages().get(0).getImageUrl() : "https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/places/default.png")
                .build();
    }

    public static PlaceResponseDTO.PlaceDatail toPlaceDetail(Place place, Integer reviewCount, Double averageRating, Boolean isBookmarked) {
        PlaceResponseDTO.PlaceDetail placeDetail = null;
        
        if (place.getBookStoreAttribute() != null) {
            placeDetail = toBookStoreDetail(place.getBookStoreAttribute());
        } else if (place.getRestaurantAttribute() != null) {
            placeDetail = toRestaurantDetail(place.getRestaurantAttribute());
        } else if (place.getTouristSpotAttribute() != null) {
            placeDetail = toTouristAttractionDetail(place.getTouristSpotAttribute());
        } else if (place.getFestivalAttribute() != null) {
            placeDetail = toFestivalDetail(place.getFestivalAttribute());
        }
        
        return PlaceResponseDTO.PlaceDatail.builder()
                .placeId(place.getId())
                .placeType(place.getPlaceType())
                .usetime(place.getUseTime())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .websiteUrl(extractHref(place.getWebsiteUrl()))
                .tel(place.getTel())
                .reviewCount(reviewCount)
                .rating(averageRating)
                .isBookmarked(isBookmarked)
                .placeDetail(placeDetail)
                .placeImageUrls(place.getPlaceImages() != null && !place.getPlaceImages().isEmpty() ? 
                    place.getPlaceImages().stream()
                        .map(img -> img.getImageUrl())
                        .toList() : List.of("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/places/default.png"))
                .build();
    }
    
    public static PlaceResponseDTO.BookStoreDetail toBookStoreDetail(BookStoreAttribute attribute) {
        return PlaceResponseDTO.BookStoreDetail.builder()
                .overview(attribute.getOverview())
                .bookCafe(attribute.isBookCafe())
                .bookStay(attribute.isBookStay())
                .parking(attribute.isParking())
                .petFriendly(attribute.isPetFriendly())
                .spaceRental(attribute.isSpaceRental())
                .reservation(attribute.isReservation())
                .readingClub(attribute.isReadingClub())
                .bookChallengeStatus(attribute.isBookChallengeStatus())
                .build();
    }
    
    public static PlaceResponseDTO.RestaurantDetail toRestaurantDetail(RestaurantAttribute attribute) {
        return PlaceResponseDTO.RestaurantDetail.builder()
                .firstmenu(attribute.getFirstmenu())
                .treatmenu(attribute.getTreatmenu())
                .kidsfacility(attribute.getKidsfacility())
                .isSmokingAllowed(attribute.getIsSmokingAllowed())
                .isTakeoutAvailable(attribute.getIsTakeoutAvailable())
                .hasParking(attribute.getHasParking())
                .isReservable(attribute.getIsReservable())
                .build();
    }
    
    public static PlaceResponseDTO.TouristAttractionDetail toTouristAttractionDetail(TouristSpotAttribute attribute) {
        return PlaceResponseDTO.TouristAttractionDetail.builder()
                .overview(attribute.getOverview())
                .parkingAvailable(attribute.getParkingAvailable())
                .petsAllowed(attribute.getPetsAllowed())
                .babyCarriageAllowed(attribute.getBabyCarriageAllowed())
                .creditCardAccepted(attribute.getCreditCardAccepted())
                .build();
    }
    
    public static PlaceResponseDTO.FestivalDetail toFestivalDetail(FestivalAttribute attribute) {
        return PlaceResponseDTO.FestivalDetail.builder()
                .overview(attribute.getOverview())
                .programs(attribute.getPrograms())
                .startDate(attribute.getStartDate())
                .endDate(attribute.getEndDate())
                .build();
    }

    public static String extractHref(String html) {
        if (html == null || html.isBlank()) return null;
        return html.replaceAll(".*href=\"([^\"]+)\".*", "$1");
    }
}