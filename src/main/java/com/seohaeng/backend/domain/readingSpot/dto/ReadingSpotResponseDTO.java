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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReadingSpotResponseDTO{
        private Long readingSpotId;
        private String address;
        private Double latitude;
        private Double longitude;
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
    }
}
