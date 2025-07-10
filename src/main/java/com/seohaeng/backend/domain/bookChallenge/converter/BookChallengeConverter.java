package com.seohaeng.backend.domain.bookChallenge.converter;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookChallengeConverter {

    public static BookChallengeProof toBookChallengeProof(BookChallengeRequestDTO.createBookChallengeProof request,
                                                          User user,
                                                          Place place) {
        return BookChallengeProof.builder()
                .bookChallengeProofContent(request.getProofContent())
                .presentMessage(request.getPresentMessage())
                .user(user)
                .place(place)
                .receivedBookTitle(request.getReceivedBookTitle())
                .receivedBookAuthor(request.getReceivedBookAuthor())
                .receivedBookImage(request.getReceivedBookImage())
                .givenBookTitle(request.getGivenBookTitle())
                .givenBookAuthor(request.getGivenBookAuthor())
                .givenBookImage(request.getGivenBookImage())
                .build();
    }
}