package com.seohaeng.backend.domain.bookChallenge.service;

import com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.*;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofCommentRepository;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofLikeRepository;
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
    private final BookChallengeProofLikeRepository bookChallengeProofLikeRepository;

    // 참여 중인 북챌린지 정보 조회
    public BookChallengeResponseDTO.saveBookChallenge getInprogressBookChallengeInfo(Long userId){
        User user = findUserById(userId);
        BookChallenge bookChallenge = bookChallengeRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_EXIST));
        if(bookChallenge.isAccepted()){
            throw new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_ALREADY_DONE);
        }
        return toSaveBookChallenge(bookChallenge, user);
    }

    // 북챌린지 인증 게시글 개별 조회
    public BookChallengeResponseDTO.getBookChallenge getBookChallenge(Long userId, Long bookChallengeProofId) {
        BookChallengeProof bookChallengeProof = findBookChallengeProofWithImages(bookChallengeProofId);
        User user = findUserById(userId);
        boolean isLikedByUser = isLikedByUser(user, bookChallengeProof);
        return convertToDTO(bookChallengeProof, isLikedByUser);
    }

    // 북챌린지 인증 게시글 전체 조회
    public BookChallengeResponseDTO.getBookChallengeListDTO getBookChallengeList(Integer page, Integer size, String sort, Long userId){
        User user = findUserById(userId);
        Sort sortOption = createSortOption(sort);
        PageRequest pageRequest = PageRequest.of(page-1, size, sortOption);
        Page<BookChallengeProof> bookChallengeProofPage = bookChallengeProofRepository.findAll(pageRequest);
        
        List<BookChallengeResponseDTO.getBookChallenge> bookChallengeDTOList = bookChallengeProofPage.getContent()
                .stream()
                .map(proof -> convertToDTO(proof, isLikedByUser(user, proof)))
                .collect(Collectors.toList());
                
        return BookChallengeConverter.toGetBookChallengeListDTO(bookChallengeDTOList, bookChallengeProofPage);
    }

    // 북챌린지 인증 게시글 댓글 조회
    public BookChallengeResponseDTO.getBookChallengeCommentListDTO getBookChallengeCommentList(Integer page, Integer size, Long bookChallengeProofId) {
        BookChallengeProof bookChallengeProof = findBookChallengeProofById(bookChallengeProofId);
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<BookChallengeProofComment> bookChallengeProofCommentPage = bookChallengeProofCommentRepository.findAllByBookChallengeProof(pageRequest, bookChallengeProof);

        List<BookChallengeResponseDTO.getBookChallengeCommentDTO> bookChallengeCommentDTOList = bookChallengeProofCommentPage.getContent()
                .stream()
                .map(BookChallengeConverter::toGetBookChallengeCommentDTO)
                .collect(Collectors.toList());

        return BookChallengeConverter.toGetBookChallengeCommentListDTO(bookChallengeCommentDTOList, bookChallengeProofCommentPage);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    private BookChallengeProof findBookChallengeProofById(Long bookChallengeProofId) {
        return bookChallengeProofRepository.findById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_PROOF_NOT_FOUND));
    }

    private BookChallengeProof findBookChallengeProofWithImages(Long bookChallengeProofId) {
        return bookChallengeProofRepository.findWithImagesById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_PROOF_NOT_FOUND));
    }

    private boolean isLikedByUser(User user, BookChallengeProof bookChallengeProof) {
        return bookChallengeProofLikeRepository.findByUserAndBookChallengeProof(user, bookChallengeProof).isPresent();
    }

    private Sort createSortOption(String sort) {
        return switch (sort) {
            case "popular" -> Sort.by(Sort.Direction.DESC, "bookChallengeProofLikes");
            case "recent" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    private BookChallengeResponseDTO.getBookChallenge convertToDTO (BookChallengeProof bookChallengeProof, boolean likedByMe){
        List<BookChallengeProofImage> imageList = bookChallengeProof.getBookChallengeProofImageList();
        List<String> imageUrls = imageList.stream()
                .sorted((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()))
                .map(BookChallengeProofImage::getImageUrl)
                .collect(Collectors.toList());
        return BookChallengeConverter.toGetBookChallengeDTO(bookChallengeProof, imageUrls, likedByMe);
    }
}