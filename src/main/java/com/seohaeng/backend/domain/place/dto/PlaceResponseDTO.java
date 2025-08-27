package com.seohaeng.backend.domain.place.dto;

import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import lombok.*;

import java.time.LocalDate;
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
        private String region;
        private String address;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookStoreDto{
        private Long placeId;
        private String name;
        private PlaceType placeType;
        private String region;
        private String address;
        private Double latitude;
        private Double longitude;
        private String imageUrl;
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookStoreListDto {
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
        private List<BookStoreDto> placeList;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceBookmarkToggleResponse{
        private boolean bookmarked;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TodayPlaceResponse{
        private Long placeId;
        private String name;
        private PlaceType placeType;
        private String overview;
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OngoingFestivalResponse{
        private Long placeId;
        private PlaceType placeType;
        private String festivalName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String imageUrl;
    }
}