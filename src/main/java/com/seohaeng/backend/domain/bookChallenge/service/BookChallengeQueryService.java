package com.seohaeng.backend.domain.bookChallenge.service;

import com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallenge;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofComment;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofImage;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofCommentRepository;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofRepository;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.BookChallengeHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter.toSaveBookChallenge;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookChallengeQueryService {

    private final BookChallengeProofRepository bookChallengeProofRepository;
    private final BookChallengeProofCommentRepository bookChallengeProofCommentRepository;
    private final UserRepository userRepository;
    private final BookChallengeRepository bookChallengeRepository;

    public BookChallengeResponseDTO.saveBookChallenge getInprogressBookChallengeInfo(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        BookChallenge bookChallenge = bookChallengeRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_EXIST));
        if(bookChallenge.isAccepted()){
            throw new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_ALREADY_DONE);
        }
        return (toSaveBookChallenge(bookChallenge,user));
    }

    public BookChallengeResponseDTO.getBookChallenge getBookChallenge(Long bookChallengeProofId) {
        BookChallengeProof bookChallengeProof = bookChallengeProofRepository.findWithImagesById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));
        return convertToDTO(bookChallengeProof);
    }

    public BookChallengeResponseDTO.getBookChallengeListDTO getBookChallengeList(Integer page, Integer size, String sort){
        Sort sortOption;
        switch (sort) {
            case "popular" -> sortOption = Sort.by(Sort.Direction.DESC, "bookChallengeProofLikes");
            case "recent" -> sortOption = Sort.by(Sort.Direction.DESC, "createdAt");
            default -> sortOption = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        PageRequest pageRequest = PageRequest.of(page-1, size, sortOption);
        Page<BookChallengeProof> bookChallengeProofPage = bookChallengeProofRepository.findAll(pageRequest);
        List<BookChallengeResponseDTO.getBookChallenge> toGetBookChallengeDTOList = bookChallengeProofPage.getContent().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
        return BookChallengeConverter.toGetBookChallengeListDTO(toGetBookChallengeDTOList, bookChallengeProofPage);
    }

    public BookChallengeResponseDTO.getBookChallengeCommentListDTO getBookChallengeCommentList(Integer page, Integer size, Long bookChallengeProofId) {
        BookChallengeProof bookChallengeProof = bookChallengeProofRepository.findById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<BookChallengeProofComment> bookChallengeProofCommentPage = bookChallengeProofCommentRepository.findAllByBookChallengeProof(pageRequest,bookChallengeProof);

        List<BookChallengeResponseDTO.getBookChallengeCommentDTO> bookChallengeCommentDTOList = bookChallengeProofCommentPage.getContent()
                .stream().map(BookChallengeConverter::toGetBookChallengeCommentDTO).collect(Collectors.toList());

        return BookChallengeConverter.toGetBookChallengeCommentListDTO(bookChallengeCommentDTOList,bookChallengeProofCommentPage);
    }

    private BookChallengeResponseDTO.getBookChallenge convertToDTO (BookChallengeProof bookChallengeProof){
        List<BookChallengeProofImage> imageList = bookChallengeProof.getBookChallengeProofImageList();
        List<String> imageUrls = imageList.stream().map(BookChallengeProofImage::getImageUrl).collect(Collectors.toList());
        return BookChallengeConverter.toGetBookChallengeDTO(bookChallengeProof, imageUrls);
    }
}