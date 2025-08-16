package com.seohaeng.backend.domain.readingSpot.service;

import com.seohaeng.backend.domain.readingSpot.repository.*;
import com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotScrap;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.ReadingSpotHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadingSpotQueryService {

    private final UserRepository userRepository;
    private final ReadingSpotRepository readingSpotRepository;
    private final ReadingSpotCommentRepository readingSpotCommentRepository;
    private final ReadingSpotScrapRepository readingSpotScrapRepository;
    private final ReadingSpotLikeRepository readingSpotLikeRepository;

    // 공간책갈피 상세 조회
    public ReadingSpotResponseDTO.GetReadingSpotResponseDTO getReadingSpot(Long readingSpotId, Long userId) {
        ReadingSpot readingSpot = findReadingSpotWithImages(readingSpotId);
        
        if (!readingSpot.isOpened()) {
            throw new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND);
        }
        
        User user = findUserById(userId);
        List<String> imageList = getSortedImageUrls(readingSpot);
        boolean isLiked = isLikedByUser(user, readingSpot);
        boolean isScraped = isScrapedByUser(user, readingSpot);

        return ReadingSpotConverter.toGetReadingSpotResponseDTO(readingSpot, imageList, isLiked, isScraped);
    }

    // 공간책갈피 댓글 리스트 조회
    public ReadingSpotResponseDTO.GetReadingSpotCommentListResponseDTO getReadingSpotCommentList(
            Long readingSpotId, Integer page, Integer size) {
        ReadingSpot readingSpot = findReadingSpotWithImages(readingSpotId);
        PageRequest pageRequest = createPageRequest(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReadingSpotComment> readingSpotCommentPage = readingSpotCommentRepository.findAllByReadingSpot(readingSpot, pageRequest);

        List<ReadingSpotResponseDTO.GetReadingSpotCommentResponseDTO> result =
                readingSpotCommentPage.getContent().stream()
                        .map(ReadingSpotConverter::toGetReadingSpotCommentResponseDTO)
                        .collect(Collectors.toList());
        return ReadingSpotConverter.toGetReadingSpotCommentListResponseDTO(result, readingSpotCommentPage);
    }

    // 내가 스크랩한 공간책갈피 리스트 조회
    public ReadingSpotResponseDTO.GetReadingSpotDetailListResponseDTO getMyScrapReadingSpotListResponseDTO(Long userId, Integer page, Integer size) {
        User user = findUserById(userId);
        PageRequest pageRequest = createPageRequest(page, size);
        Page<ReadingSpotScrap> readingSpotScrapPage = readingSpotScrapRepository.findAllByUser(user, pageRequest);

        List<ReadingSpotResponseDTO.GetReadingSpotResponseDTO> readingSpotResponseDTOS = readingSpotScrapPage.getContent()
                .stream()
                .map(ReadingSpotScrap::getReadingSpot)
                .filter(ReadingSpot::isOpened)
                .map(readingSpot -> convertToDetailResponseDTO(readingSpot, user))
                .collect(Collectors.toList());

        return ReadingSpotConverter.toGetReadingSpotDetailListResponseDTO(readingSpotResponseDTOS, readingSpotScrapPage);
    }

    // 내가 등록한 공간책갈피 리스트 조회
    public ReadingSpotResponseDTO.GetReadingSpotDetailListResponseDTO getMyReadingSpotListResponseDTO(Long userId, Integer page, Integer size) {
        User user = findUserById(userId);
        PageRequest pageRequest = createPageRequest(page, size);
        Page<ReadingSpot> readingSpotPage = readingSpotRepository.findAllByUser(user, pageRequest);

        List<ReadingSpotResponseDTO.GetReadingSpotResponseDTO> readingSpotResponseDTOS = readingSpotPage.getContent()
                .stream()
                .map(readingSpot -> convertToDetailResponseDTO(readingSpot, user))
                .collect(Collectors.toList());

        return ReadingSpotConverter.toGetReadingSpotDetailListResponseDTO(readingSpotResponseDTOS, readingSpotPage);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    private ReadingSpot findReadingSpotWithImages(Long readingSpotId) {
        return readingSpotRepository.findWithReadingSpotImagesById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));
    }

    private PageRequest createPageRequest(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private PageRequest createPageRequest(Integer page, Integer size, Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

    private List<String> getSortedImageUrls(ReadingSpot readingSpot) {
        return readingSpot.getReadingSpotImageList()
                .stream()
                .sorted((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()))
                .map(ReadingSpotImage::getImageUrl)
                .collect(Collectors.toList());
    }

    private boolean isLikedByUser(User user, ReadingSpot readingSpot) {
        return readingSpotLikeRepository.findByUserAndReadingSpot(user, readingSpot).isPresent();
    }

    private boolean isScrapedByUser(User user, ReadingSpot readingSpot) {
        return readingSpotScrapRepository.findByUserAndReadingSpot(user, readingSpot).isPresent();
    }

    private ReadingSpotResponseDTO.GetReadingSpotResponseDTO convertToDetailResponseDTO(ReadingSpot readingSpot, User user) {
        List<String> imageList = getSortedImageUrls(readingSpot);
        boolean isLiked = isLikedByUser(user, readingSpot);
        boolean isScraped = isScrapedByUser(user, readingSpot);
        return ReadingSpotConverter.toGetReadingSpotResponseDTO(readingSpot, imageList, isLiked, isScraped);
    }
}
