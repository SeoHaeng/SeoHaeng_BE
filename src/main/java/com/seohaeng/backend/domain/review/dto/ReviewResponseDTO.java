package com.seohaeng.backend.domain.review.dto;

import lombok.*;

public class ReviewResponseDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateReviewResponseDTO{
        private Long reviewId;
    }
}