package com.seohaeng.backend.domain.place.converter;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
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
}