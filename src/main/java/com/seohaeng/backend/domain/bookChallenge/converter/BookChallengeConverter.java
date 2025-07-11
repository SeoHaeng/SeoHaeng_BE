package com.seohaeng.backend.domain.bookChallenge.converter;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static BookChallengeResponseDTO.getBookChallenge toGetBookChallengeDTO(BookChallengeProof proof, List<String> images) {
        return BookChallengeResponseDTO.getBookChallenge.builder()
                .createdAt(proof.getCreatedAt().toLocalDate())
                .creatorId(proof.getUser().getId())
                .bookChallengeId(proof.getId())
                .presentMessage(proof.getPresentMessage())
                .proofContent(proof.getBookChallengeProofContent())
                .likes(proof.getBookChallengeProofLikes())
                .receivedBookTitle(proof.getReceivedBookTitle())
                .receivedBookAuthor(proof.getReceivedBookAuthor())
                .receivedBookImage(proof.getReceivedBookImage())
                .givenBookTitle(proof.getGivenBookTitle())
                .givenBookAuthor(proof.getGivenBookAuthor())
                .givenBookImage(proof.getGivenBookImage())
                .proofImageUrls(images)
                .build();
    }

    public static BookChallengeResponseDTO.getBookChallengeListDTO toGetBookChallengeListDTO(List<BookChallengeResponseDTO.getBookChallenge> list,
                                                                                             Page<BookChallengeProof> bookChallengeProofPage){
        return BookChallengeResponseDTO.getBookChallengeListDTO.builder()
                .listSize(list.size())
                .totalPage(bookChallengeProofPage.getTotalPages())
                .totalElements(bookChallengeProofPage.getTotalElements())
                .isFirst(bookChallengeProofPage.isFirst())
                .isLast(bookChallengeProofPage.isLast())
                .getBookChallengeList(list)
                .build();
    }
}