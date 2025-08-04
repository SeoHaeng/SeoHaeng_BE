package com.seohaeng.backend.domain.readingSpot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReadingSpotResponseDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateReadingSpotResponseDTO{
        private Long readingSpotId;
    }
}
