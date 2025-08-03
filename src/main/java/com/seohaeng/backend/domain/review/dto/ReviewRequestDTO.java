package com.seohaeng.backend.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReviewRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewRequestDTO {

        @NotNull(message = "평점은 필수입니다.")
        @DecimalMin(value = "0.5", inclusive = true, message = "평점은 최소 0.5 이상이어야 합니다.")
        @DecimalMax(value = "5.0", inclusive = true, message = "평점은 최대 5.0 이하여야 합니다.")
        private BigDecimal rating;

        @NotNull(message = "방문일은 필수입니다.")
        private LocalDate visitedDate;

        @NotBlank(message = "리뷰 내용은 필수입니다.")
        private String content;
    }
}
