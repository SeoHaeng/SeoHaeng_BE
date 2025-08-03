package com.seohaeng.backend.domain.review.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReviewResponseDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateReviewResponseDTO{
        private Long reviewId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReviewResponseDTO {
        LocalDate createdAt;
        Long creatorId;
        Long PlaceId;
        Long ReviewId;

        BigDecimal rating;
        String reviewContent;
        List<String> reviewImageList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetReviewListResponseDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        Double totalReviewRating;
        List<GetReviewResponseDTO> getReviewList;
    }
}