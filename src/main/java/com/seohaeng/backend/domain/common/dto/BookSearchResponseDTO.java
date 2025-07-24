package com.seohaeng.backend.domain.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BookSearchResponseDTO {

    @NoArgsConstructor
    @Getter
    public static class SearchResponseDTO {
        private String lastBuildDate;
        private int total;
        private int start;
        private int display;
        private List<BookItemDTO> items;
    }

    @NoArgsConstructor
    @Getter
    public static class BookItemDTO {
        private String title;
        private String link;
        private String image;
        private String author;
        private String price;
        private String discount;
        private String publisher;
        private String pubdate;
        private String isbn;
    }
}
