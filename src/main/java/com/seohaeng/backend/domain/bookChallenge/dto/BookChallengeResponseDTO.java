package com.seohaeng.backend.domain.bookChallenge.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        Long bookChallengeId;

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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getBookChallengeListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        List<getBookChallenge> getBookChallengeList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getBookChallengeCommentDTO {
        LocalDateTime createdAt;
        Long userId;
        String nickname;
        String userProfileImageUrl;
        String comment;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getBookChallengeCommentListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
        List<getBookChallengeCommentDTO> getBookChallengeCommentList;
    }
}