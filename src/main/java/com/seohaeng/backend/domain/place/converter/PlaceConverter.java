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
                    ? place.getPlaceImages().get(0).getImageUrl() : null)
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
                    ? place.getPlaceImages().get(0).getImageUrl() : null)
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
                    ? place.getPlaceImages().get(0).getImageUrl() : null)
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
                .websiteUrl(place.getWebsiteUrl())
                .tel(place.getTel())
                .reviewCount(reviewCount)
                .rating(averageRating)
                .isBookmarked(isBookmarked)
                .placeDetail(placeDetail)
                .placeImageUrls(place.getPlaceImages() != null ? 
                    place.getPlaceImages().stream()
                        .map(img -> img.getImageUrl())
                        .toList() : List.of())
                .build();
    }
    
    public static PlaceResponseDTO.BookStoreDetail toBookStoreDetail(BookStoreAttribute attribute) {
        return PlaceResponseDTO.BookStoreDetail.builder()
                .bookCafe(attribute.isBookCafe())
                .bookStay(attribute.isBookStay())
                .bookChallengeStatus(attribute.isBookChallengeStatus())
                .salonAll(attribute.isSalonAll())
                .readingClub(attribute.isReadingClub())
                .bookTalk(attribute.isBookTalk())
                .lecture(attribute.isLecture())
                .originalContent(attribute.isOriginalContent())
                .bookWellage(attribute.isBookWellage())
                .convenienceAll(attribute.isConvenienceAll())
                .spaceRental(attribute.isSpaceRental())
                .parking(attribute.isParking())
                .petFriendly(attribute.isPetFriendly())
                .bookStorage(attribute.isBookStorage())
                .creatorSupport(attribute.isCreatorSupport())
                .bookOrder(attribute.isBookOrder())
                .bookDelivery(attribute.isBookDelivery())
                .collectionAll(attribute.isCollectionAll())
                .indiePublication(attribute.isIndiePublication())
                .usedBooks(attribute.isUsedBooks())
                .goods(attribute.isGoods())
                .artBook(attribute.isArtBook())
                .illustrationBook(attribute.isIllustrationBook())
                .giftShop(attribute.isGiftShop())
                .souvenirs(attribute.isSouvenirs())
                .tasteAll(attribute.isTasteAll())
                .pub(attribute.isPub())
                .cafe(attribute.isCafe())
                .snack(attribute.isSnack())
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
}