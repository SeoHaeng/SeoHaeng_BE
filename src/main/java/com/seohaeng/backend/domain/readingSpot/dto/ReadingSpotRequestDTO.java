package com.seohaeng.backend.domain.readingSpot.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ReadingSpotRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadingSpotCreateRequestDTO {
        @NotBlank(message = "주소는 필수 입력값입니다.")
        private String address;

        @NotNull(message = "위도는 필수 입력값입니다.")
        @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다.")
        @DecimalMax(value = "90.0", message = "위도는 90.0 이하여야 합니다.")
        private Double latitude;

        @NotNull(message = "경도는 필수 입력값입니다.")
        @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다.")
        @DecimalMax(value = "180.0", message = "경도는 180.0 이하여야 합니다.")
        private Double longitude;

        @Min(value = 1, message = "템플릿 ID는 1 이상이어야 합니다.")
        @Max(value = 4, message = "템플릿 ID는 4 이하여야 합니다.")
        private int templateId;

        @NotBlank(message = "제목은 필수 입력값입니다.")
        @Size(min = 1, max = 12, message = "제목은 1자 이상 12자 이하이어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력값입니다.")
        @Size(min = 1, max = 300, message = "내용은 1자 이상 300자 이하이어야 합니다.")
        private String content;

        @NotBlank(message = "책 제목은 필수입니다.")
        private String bookTitle;

        @NotBlank(message = "책 저자는 필수입니다.")
        private String bookAuthor;

        @NotBlank(message = "책 이미지 URL은 필수입니다.")
        private String bookImage;

        @NotNull(message = "책 출간일은 필수입니다.")
        private LocalDate bookPubDate;

        private boolean opened;
    }
}
