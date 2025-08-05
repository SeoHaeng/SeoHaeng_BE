package com.seohaeng.backend.domain.readingSpot.service;

import com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofLike;
import com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository.*;
import com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.*;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.BookChallengeHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.ReadingSpotHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import com.seohaeng.backend.global.security.handler.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingSpotCommandService {

    private final UserRepository userRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final ReadingSpotRepository readingSpotRepository;
    private final ReadingSpotImageRepository readingSpotImageRepository;
    private final ReadingSpotCommentRepository readingSpotCommentRepository;
    private final ReadingSpotLikeRepository readingSpotLikeRepository;
    private final ReadingSpotScrapRepository readingSpotScrapRepository;

    @Transactional
    public ReadingSpotResponseDTO.CreateReadingSpotResponseDTO createReadingSpot(
            @AuthUser Long userId, List<MultipartFile> images,
            ReadingSpotRequestDTO.ReadingSpotCreateRequestDTO request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ReadingSpot newReadingSpot = ReadingSpotConverter.toReadingSpot(request, user);
        readingSpotRepository.save(newReadingSpot);

        int mainImageIndex = request.getMainImageIndex();

        List<ReadingSpotImage> readingSpotImageList = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                final String uuid = UUID.randomUUID().toString();
                final String keyName = amazonS3Manager.generateReadingSpotKeyName(uuid);
                final String imageUrl = amazonS3Manager.uploadFile(keyName, image);

                ReadingSpotImage newReadingSpotImage = ReadingSpotImage.builder()
                        .imageUrl(imageUrl)
                        .readingSpot(newReadingSpot)
                        .isMain(i == mainImageIndex)
                        .build();

                readingSpotImageList.add(newReadingSpotImage);
            }
            readingSpotImageRepository.saveAll(readingSpotImageList);
        }
        return new ReadingSpotResponseDTO.CreateReadingSpotResponseDTO(newReadingSpot.getId());
    }

    @Transactional
    public ReadingSpotResponseDTO.CreateReadingSpotCommentResponseDTO createReadingSpotComment(
            @AuthUser Long userId, Long readingSpotId,
            ReadingSpotRequestDTO.ReadingSpotCommentCreateRequestDTO request
    ){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ReadingSpot readingSpot = readingSpotRepository.findWithReadingSpotImagesById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));

        ReadingSpotComment newReadingSpotComment = ReadingSpotConverter.toReadingSpotComment(request, user, readingSpot);
        readingSpotCommentRepository.save(newReadingSpotComment);

        return new ReadingSpotResponseDTO.CreateReadingSpotCommentResponseDTO(newReadingSpotComment.getId());
    }

    @Transactional
    public ReadingSpotResponseDTO.GetReadingSpotLikeInfoDTO toggleReadingSpotLikes(Long userId, Long readingSpotId) {

        ReadingSpot readingSpot = readingSpotRepository.findById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<ReadingSpotLike> userLike
                = readingSpotLikeRepository.findByUserAndReadingSpot(user,readingSpot);

        if (userLike.isPresent()) {
            readingSpotLikeRepository.delete(userLike.get());
            readingSpot.decreaseReadingSpotLikes();
        } else {
            ReadingSpotLike newLike = ReadingSpotLike.builder()
                    .user(user)
                    .readingSpot(readingSpot)
                    .build();
            readingSpotLikeRepository.save(newLike);
            readingSpot.increaseReadingSpotLikes();
        }
        return new ReadingSpotResponseDTO.GetReadingSpotLikeInfoDTO(readingSpot.getLikes());
    }

    @Transactional
    public ReadingSpotResponseDTO.GetReadingSpotScrapInfoDTO toggleReadingSpotScraps(Long userId, Long readingSpotId) {

        ReadingSpot readingSpot = readingSpotRepository.findById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<ReadingSpotScrap> userScrap
                = readingSpotScrapRepository.findByUserAndReadingSpot(user,readingSpot);

        if (userScrap.isPresent()) {
            readingSpotScrapRepository.delete(userScrap.get());
            readingSpot.decreaseReadingSpotScraps();
        } else {
            ReadingSpotScrap newScrap = ReadingSpotScrap.builder()
                    .user(user)
                    .readingSpot(readingSpot)
                    .build();
            readingSpotScrapRepository.save(newScrap);
            readingSpot.increaseReadingSpotScraps();
        }
        return new ReadingSpotResponseDTO.GetReadingSpotScrapInfoDTO(readingSpot.getScraps());
    }
}
