package com.seohaeng.backend.domain.place.converter;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.place.Place;
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

    public static PlaceResponseDTO.placeListDto toplaceListDto (Page<?> page,
                                                                List<PlaceResponseDTO.placeDto> placeDtoList) {
        return PlaceResponseDTO.placeListDto.builder()
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
                .imageUrl(place.getPlaceImages().get(0).getImageUrl())
                .placeType(place.getPlaceType())
                .build();
    }
}