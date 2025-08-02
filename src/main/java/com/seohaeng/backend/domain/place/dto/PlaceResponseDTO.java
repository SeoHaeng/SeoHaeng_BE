package com.seohaeng.backend.domain.place.dto;

import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import lombok.*;

import java.util.List;

public class PlaceResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class placeDto{
        private Long id;
        private String name;
        private PlaceType placeType;
        private String address;
        private String description;
        private String introduction;
        private String websiteUrl;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class placeListDto{
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
        private List<placeDto> placeList;
    }
}