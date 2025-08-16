package com.seohaeng.backend.domain.readingSpot.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ReadingSpotResponseDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateReadingSpotResponseDTO{
        private Long readingSpotId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateReadingSpotCommentResponseDTO{
        private Long commentId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class GetReadingSpotCommentListResponseDTO{
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
        private List<GetReadingSpotCommentResponseDTO> comments;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class GetReadingSpotCommentResponseDTO{
        private Long commentId;
        private LocalDate createdAt;
        private Long userId;
        private String commentContent;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class GetReadingSpotResponseDTO{
        private Long userId;
        private String userNickname;
        private String userProfilImage;

        private Long readingSpotId;
        private String region;
        private String address;
        private Double latitude;
        private Double longitude;
        private LocalDate createdAt;

        private int templateId;
        private String title;
        private String content;
        private List<String> readingSpotImages;

        private String bookTitle;
        private String bookAuthor;
        private String bookImage;
        private LocalDate bookPubDate;
        private int likes;
        private int scraps;
        private boolean opened;
        private boolean isLiked;
        private boolean isScraped;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReadingSpotLikeInfoDTO {
        Integer nowLikeCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReadingSpotScrapInfoDTO {
        Integer nowScrapCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReadingSpotItemResponseDTO{
        private String title;
        private String imageUrl;
        private String address;
        private Double latitude;
        private Double longitude;
        private int templateId;
        private Long readingSpotId;
        private LocalDate createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReadingSpotItemListResponseDTO{
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
        private List<GetReadingSpotItemResponseDTO> scrapList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReadingSpotDetailListResponseDTO{
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
        private List<GetReadingSpotResponseDTO> readingSpotList;
    }
}
