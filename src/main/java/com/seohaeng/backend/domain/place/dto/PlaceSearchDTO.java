package com.seohaeng.backend.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceSearchDTO {
    private Long placeId;
    private String name;
    private String placeType;
    private String address;

}
