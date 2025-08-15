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

        // 선물 받은 책
        String receivedBookTitle;
        String receivedBookAuthor;
        String receivedBookImage;

        // 선물 할 책
        String givenBookTitle;
        String givenBookAuthor;
        String givenBookImage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class saveBookChallenge{
        String userNickName;

        // 선물 받은 책
        String receivedBookTitle;
        String receivedBookAuthor;
        String receivedBookImage;

        // 선물 할 책
        String givenBookTitle;
        String givenBookAuthor;
        String givenBookImage;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createBookChallengeProofComment{
        String commentContent;
    }
}