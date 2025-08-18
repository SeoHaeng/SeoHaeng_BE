package com.seohaeng.backend.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceMarkerDTO {
    private Long placeId;
    private String name;
    private Double latitude;
    private Double longitude;

}
