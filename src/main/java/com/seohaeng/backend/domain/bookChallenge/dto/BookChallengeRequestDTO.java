package com.seohaeng.backend.domain.bookChallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BookChallengeRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createBookChallengeProof{
        Long bookStoreId;

        String presentMessage;
        String proofContent;

        String receivedBookTitle;
        String receivedBookAuthor;
        String receivedBookImage;

        String givenBookTitle;
        String givenBookAuthor;
        String givenBookImage;
    }
}