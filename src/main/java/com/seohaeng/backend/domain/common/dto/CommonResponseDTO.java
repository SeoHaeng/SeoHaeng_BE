package com.seohaeng.backend.domain.common.dto;

import lombok.*;

import java.util.List;

public class CommonResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class bookSearchResultDTO {
        private String title;
        private String author;
        private String bookImage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class bookSearchResultListDTO {
        private List<bookSearchResultDTO> bookSearchResults;
    }
}