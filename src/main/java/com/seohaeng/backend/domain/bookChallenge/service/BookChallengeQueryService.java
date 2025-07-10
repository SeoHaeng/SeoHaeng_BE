package com.seohaeng.backend.domain.bookChallenge.service;

import com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofImage;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.BookChallengeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookChallengeQueryService {

    private final BookChallengeProofRepository bookChallengeProofRepository;

    public BookChallengeResponseDTO.getBookChallenge getBookChallenge(Long bookChallengeProofId) {

        BookChallengeProof bookChallengeProof = bookChallengeProofRepository.findWithImagesById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));

        List<BookChallengeProofImage> imageList = bookChallengeProof.getBookChallengeProofImageList();
        List<String> imageUrls = imageList.stream().map(BookChallengeProofImage::getImageUrl).collect(Collectors.toList());

        return BookChallengeConverter.toGetBookChallengeDTO(bookChallengeProof, imageUrls);
    }
}