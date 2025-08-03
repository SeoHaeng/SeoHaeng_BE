package com.seohaeng.backend.domain.review.service;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.review.converter.ReviewConverter;
import com.seohaeng.backend.domain.review.dto.ReviewRequestDTO;
import com.seohaeng.backend.domain.review.dto.ReviewResponseDTO;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.review.entity.ReviewImage;
import com.seohaeng.backend.domain.review.repository.ReviewImageRepository;
import com.seohaeng.backend.domain.review.repository.ReviewRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.ReviewHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    public ReviewResponseDTO.CreateReviewResponseDTO createReview(
            ReviewRequestDTO.CreateReviewRequestDTO request,
            Long userId,
            Long placeId,
            List<MultipartFile> images) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId).
                orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));

        if (request.getRating().multiply(BigDecimal.valueOf(2)).stripTrailingZeros().scale() > 0) {
            throw new ReviewHandler(ErrorStatus.REVIEW_INVALID_ISSUE);
        }

        Review newReview = ReviewConverter.toReview(request, user, place);
        reviewRepository.save(newReview);

        List<ReviewImage> reviewImageList = new ArrayList<>();
        if(images != null && !images.isEmpty()){
            for (MultipartFile image : images) {
                final String uuid = UUID.randomUUID().toString();
                final String keyName = amazonS3Manager.generateReviewKeyName(uuid);
                final String imageUrl = amazonS3Manager.uploadFile(keyName, image);

                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(imageUrl)
                        .review(newReview)
                        .build();
                reviewImageList.add(reviewImage);
            }
            reviewImageRepository.saveAll(reviewImageList);
        }
        return new ReviewResponseDTO.CreateReviewResponseDTO(newReview.getId());
    }
}