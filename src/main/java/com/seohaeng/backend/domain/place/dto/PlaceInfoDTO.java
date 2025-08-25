package com.seohaeng.backend.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceInfoDTO {
    private Long placeId;
    private String name;
    private String placeType;
    private boolean bookmarked;
    private double averageRating;
    private int reviewCount;
    private double distance;
    private String address;

}
