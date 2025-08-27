package com.seohaeng.backend.domain.place.dto;

import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class PlaceResponseDTO {

    public interface PlaceDetail {
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceDatail{
        private Long placeId;
        private PlaceType placeType;
        private String name;
        private String address;
        private Double latitude;
        private Double longitude;
        private String websiteUrl;
        private String tel;

        private Integer reviewCount;
        private Double rating;
        private Boolean isBookmarked;

        private PlaceDetail placeDetail;
        private List<String> placeImageUrls;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookStoreDetail implements PlaceDetail {
        // 북카페, 북스테이
        private boolean bookCafe;
        private boolean bookStay;

        private boolean bookChallengeStatus = false;

        // 북살롱
        private boolean salonAll;
        private boolean readingClub;
        private boolean bookTalk;
        private boolean lecture;
        private boolean originalContent;
        private boolean bookWellage;

        // 편의 정보
        private boolean convenienceAll;
        private boolean spaceRental;
        private boolean parking;
        private boolean petFriendly;
        private boolean bookStorage;
        private boolean creatorSupport;
        private boolean bookOrder;
        private boolean bookDelivery;

        // 컬렉션북
        private boolean collectionAll;
        private boolean indiePublication;
        private boolean usedBooks;
        private boolean goods;
        private boolean artBook;
        private boolean illustrationBook;
        private boolean giftShop;
        private boolean souvenirs;

        // 테이스트
        private boolean tasteAll;
        private boolean pub;
        private boolean cafe;
        private boolean snack;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RestaurantDetail implements PlaceDetail {
        private String firstmenu;
        private String treatmenu;
        private String kidsfacility;
        private String isSmokingAllowed;
        private String isTakeoutAvailable;
        private String hasParking;
        private String isReservable;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TouristAttractionDetail implements PlaceDetail {
        private String overview;
        private String parkingAvailable;
        private String petsAllowed;
        private String babyCarriageAllowed;
        private String creditCardAccepted;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FestivalDetail implements PlaceDetail {
        private String overview;
        private String programs;
        private LocalDate startDate;
        private LocalDate endDate;
    }

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