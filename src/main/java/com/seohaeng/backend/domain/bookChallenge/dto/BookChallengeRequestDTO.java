package com.seohaeng.backend.domain.bookChallenge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class BookChallengeRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createBookChallengeProof{
        String presentMessage;
        String proofContent;

        @NotNull(message = "대표 이미지 인덱스는 필수입니다.")
        private Integer mainImageIndex;
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
        LocalDate receivedBookPubDate;

        // 선물 할 책
        String givenBookTitle;
        String givenBookAuthor;
        String givenBookImage;
        LocalDate givenBookPubDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createBookChallengeProofComment{
        String commentContent;
    }
}