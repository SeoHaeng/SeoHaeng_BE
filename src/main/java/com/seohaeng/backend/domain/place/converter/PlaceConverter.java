package com.seohaeng.backend.domain.place.converter;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.Place;
import org.springframework.data.domain.Page;

import java.util.List;

public class PlaceConverter {

    public static PlaceResponseDTO.placeDto toplaceDto (Place place) {
     return PlaceResponseDTO.placeDto.builder()
             .id(place.getId())
             .name(place.getName())
             .placeType(place.getPlaceType())
             .introduction(place.getIntroduction())
             .address(place.getAddress())
             .websiteUrl(place.getWebsiteUrl())
             .latitude(place.getLatitude())
             .longitude(place.getLongitude())
             .build();
    }

    public static PlaceResponseDTO.placeListDto toplaceListDto (Page<Place> placePage,
                                                                List<PlaceResponseDTO.placeDto> placeDtoList) {
        return PlaceResponseDTO.placeListDto.builder()
                .listSize(placeDtoList.size())
                .totalPage(placePage.getTotalPages())
                .totalElements(placePage.getTotalElements())
                .isFirst(placePage.isFirst())
                .isLast(placePage.isLast())
                .placeList(placeDtoList)
                .build();
    }
}