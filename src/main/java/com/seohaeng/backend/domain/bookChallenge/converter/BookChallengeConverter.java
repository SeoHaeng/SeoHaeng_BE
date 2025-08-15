package com.seohaeng.backend.domain.bookChallenge.converter;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallenge;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofComment;
import com.seohaeng.backend.domain.place.entity.place.Place;
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

    public static BookChallengeProofComment toBookChallengeProofComment(User user,
                                                                        BookChallengeProof bookChallengeProof,
                                                                        BookChallengeRequestDTO.createBookChallengeProofComment request){
        return BookChallengeProofComment.builder()
                .bookChallengeProofCommentContent(request.getCommentContent())
                .bookChallengeProof(bookChallengeProof)
                .user(user)
                .build();
    }

    public static BookChallengeResponseDTO.getBookChallengeCommentDTO toGetBookChallengeCommentDTO(BookChallengeProofComment comment) {
        User user = comment.getUser();
        return BookChallengeResponseDTO.getBookChallengeCommentDTO.builder()
                .createdAt(comment.getCreatedAt())
                .userId(user.getId())
                .nickname(user.getNickname())
                .userProfileImageUrl(user.getImageUrl())
                .comment(comment.getBookChallengeProofCommentContent())
                .build();
    }

    public static BookChallengeResponseDTO.getBookChallengeCommentListDTO toGetBookChallengeCommentListDTO (
            List<BookChallengeResponseDTO.getBookChallengeCommentDTO> getBookChallengeCommentList,
            Page<BookChallengeProofComment> bookChallengeProofCommentPage){
        return BookChallengeResponseDTO.getBookChallengeCommentListDTO.builder()
                .listSize(getBookChallengeCommentList.size())
                .totalPage(bookChallengeProofCommentPage.getTotalPages())
                .totalElements(bookChallengeProofCommentPage.getTotalElements())
                .isFirst(bookChallengeProofCommentPage.isFirst())
                .isLast(bookChallengeProofCommentPage.isLast())
                .getBookChallengeCommentList(getBookChallengeCommentList)
                .build();
    }

    public static BookChallengeResponseDTO.getBookChallengeLikeInfoDTO togetBookChallengeLikeInfoDTO(Integer count){
        return BookChallengeResponseDTO.getBookChallengeLikeInfoDTO.builder()
                .nowLikeCount(count).build();
    }

    public static BookChallenge toBookChallengeBooks (BookChallengeRequestDTO.saveBookChallenge request
            , User user, String bookStoreName){
        return BookChallenge.builder()
                .user(user)
                .bookStoreName(bookStoreName)
                .receivedBookTitle(request.getReceivedBookTitle())
                .receivedBookAuthor(request.getReceivedBookAuthor())
                .receivedBookImage(request.getReceivedBookImage())
                .givenBookTitle(request.getGivenBookTitle())
                .givenBookAuthor(request.getGivenBookAuthor())
                .givenBookImage(request.getGivenBookImage())
                .build();
    }

    public static BookChallengeResponseDTO.saveBookChallenge toSaveBookChallenge(BookChallenge bookChallenge, User user){
        return BookChallengeResponseDTO.saveBookChallenge.builder()
                .bookChallengeId(bookChallenge.getId())
                .userNickName(user.getNickname())
                .bookStoreName(bookChallenge.getBookStoreName())
                .receivedBookTitle(bookChallenge.getReceivedBookTitle())
                .receivedBookAuthor(bookChallenge.getReceivedBookAuthor())
                .receivedBookImage(bookChallenge.getReceivedBookImage())
                .givenBookTitle(bookChallenge.getGivenBookTitle())
                .givenBookAuthor(bookChallenge.getGivenBookAuthor())
                .givenBookImage(bookChallenge.getGivenBookImage())
                .build();
    }
}