package com.seohaeng.backend.domain.bookChallenge.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class BookChallengeResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getBookChallenge {
        LocalDate createdAt;
        Long creatorId;

        String presentMessage;
        String proofContent;
        int likes;

        String receivedBookTitle;
        String receivedBookAuthor;
        String receivedBookImage;

        String givenBookTitle;
        String givenBookAuthor;
        String givenBookImage;

        List<String> proofImageUrls;
    }
}