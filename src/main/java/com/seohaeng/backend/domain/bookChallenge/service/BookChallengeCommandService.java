package com.seohaeng.backend.domain.bookChallenge.service;

import com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.*;
import com.seohaeng.backend.domain.bookChallenge.repository.*;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import com.seohaeng.backend.domain.user.entity.Owner;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.BookChallengeHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter.toBookChallengeBooks;

@Transactional
@Service
@RequiredArgsConstructor
public class BookChallengeCommandService {

    private final UserRepository userRepository;
    private final BookChallengeProofRepository bookChallengeProofRepository;
    private final BookChallengeProofImageRepository bookChallengeProofImageRepository;
    private final BookChallengeProofCommentRepository bookChallengeProofCommentRepository;
    private final BookChallengeProofLikeRepository bookChallengeProofLikeRepository;
    private final BookChallengeRepository bookChallengeRepository;

    private final AmazonS3Manager amazonS3Manager;

    // 북챌린지 인증 글 생성
    public void createBookChallengeProof(
            Long bookChallengeId,
            BookChallengeRequestDTO.createBookChallengeProof request,
            Long userId,
            List<MultipartFile> images) {

        BookChallenge bookChallenge = bookChallengeRepository.findById(bookChallengeId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));

        if(bookChallenge.isAccepted()){
            throw new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_ALREADY_DONE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        BookChallengeProof bookChallengeProof
                = BookChallengeConverter.toBookChallengeProof(bookChallenge, user, request);
        bookChallengeProofRepository.save(bookChallengeProof);

        int mainImageIndex = request.getMainImageIndex();

        List<BookChallengeProofImage> bookChallengeProofImageList = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                final String uuid = UUID.randomUUID().toString();
                final String keyName = amazonS3Manager.generateBookChallengeProofKeyName(uuid);
                final String imageUrl = amazonS3Manager.uploadFile(keyName, image);

                BookChallengeProofImage newBookChallengeImage = BookChallengeProofImage.builder()
                        .imageUrl(imageUrl)
                        .isMain(i == mainImageIndex)
                        .bookChallengeProof(bookChallengeProof)
                        .build();

                bookChallengeProofImageList.add(newBookChallengeImage);
            }
            bookChallenge.setAccepted(true);
            bookChallengeProofImageRepository.saveAll(bookChallengeProofImageList);
        }
    }

    // 북 챌린지 인증 글 삭제
    public void deleteBookChallengeProof(Long userId, Long bookChallengeProofId) {
        BookChallengeProof bookChallengeProof = bookChallengeProofRepository.findWithImagesById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));
        if (!bookChallengeProof.getUser().getId().equals(userId)) {
            throw new BookChallengeHandler(ErrorStatus._FORBIDDEN);
        }
        List<String> images = bookChallengeProof.getBookChallengeProofImageList()
                .stream().map(BookChallengeProofImage::getImageUrl)
                .collect(Collectors.toList());
        bookChallengeProofRepository.delete(bookChallengeProof);
        images.forEach(amazonS3Manager::deleteFile);
    }

    // 북 챌린지 인증 글 댓글 생성
    public void createBookChallengeProofComment(Long userId,
                                                Long bookChallengeProofId,
                                                BookChallengeRequestDTO.createBookChallengeProofComment request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        BookChallengeProof bookChallengeProof = bookChallengeProofRepository.findWithImagesById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));
        BookChallengeProofComment bookChallengeProofComment = BookChallengeConverter.toBookChallengeProofComment(user, bookChallengeProof, request);
        bookChallengeProofCommentRepository.save(bookChallengeProofComment);
    }

    // 북 챌린지 인증 글 좋아요 토글
    public BookChallengeResponseDTO.getBookChallengeLikeInfoDTO toggleBookChallengeProofLike(Long userId, Long bookChallengeProofId) {

        BookChallengeProof bookChallengeProof = bookChallengeProofRepository.findById(bookChallengeProofId)
                .orElseThrow(() -> new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<BookChallengeProofLike> userLike = bookChallengeProofLikeRepository.findByUserAndBookChallengeProof(user,bookChallengeProof);

        if (userLike.isPresent()) {
            bookChallengeProofLikeRepository.delete(userLike.get());
            bookChallengeProof.decreaseBookChallengeProofLikes();
        } else {
            BookChallengeProofLike newLike = BookChallengeProofLike.builder()
                    .user(user)
                    .bookChallengeProof(bookChallengeProof)
                    .build();
            bookChallengeProofLikeRepository.save(newLike);
            bookChallengeProof.increaseBookChallengeProofLikes();
        }
        return BookChallengeConverter.togetBookChallengeLikeInfoDTO(bookChallengeProof.getBookChallengeProofLikes());
    }

    // 북 챌린지 생성
    public void saveNewBookChallenge(Long userId, BookChallengeRequestDTO.saveBookChallenge request) {

        // Owner 권한 체크
        Owner owner = userRepository.findById(userId)
                .map(User::getOwner)
                .orElseThrow(() -> new UserHandler(ErrorStatus.OWNER_NOT_FOUND));

        // 챌린저 조회
        User challenger = userRepository.findByNickname(request.getUserNickName())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 최근 챌린지 중복 확인
        Optional<BookChallenge> bookChallenge = bookChallengeRepository.findFirstByUserOrderByCreatedAtDesc(challenger);
        if(bookChallenge.isPresent()) {
            if(!bookChallenge.get().isAccepted()){
                throw new BookChallengeHandler(ErrorStatus.BOOK_CHALLENGE_ALREADY_EXIST);
            }
        }

        // 새로운 챌린지 등록
        BookChallenge newBookChallenge = toBookChallengeBooks(request, challenger, owner.getBookStoreName());
        bookChallengeRepository.save(newBookChallenge);
    }
}