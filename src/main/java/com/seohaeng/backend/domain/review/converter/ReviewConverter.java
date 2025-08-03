package com.seohaeng.backend.domain.review.converter;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.review.dto.ReviewRequestDTO;
import com.seohaeng.backend.domain.review.dto.ReviewResponseDTO;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public class ReviewConverter {

    public static Review toReview(ReviewRequestDTO.CreateReviewRequestDTO reviewRequestDTO, User user, Place place) {
        return Review.builder()
                .place(place)
                .content(reviewRequestDTO.getContent())
                .rating(reviewRequestDTO.getRating())
                .visitedDate(reviewRequestDTO.getVisitedDate())
                .user(user)
                .build();
    }

    public static ReviewResponseDTO.GetReviewResponseDTO toGetReviewResponseDTO(Review review, List<String> reviewImageList) {
        return ReviewResponseDTO.GetReviewResponseDTO.builder()
                .createdAt(review.getCreatedAt().toLocalDate())
                .creatorId(review.getUser().getId())
                .PlaceId(review.getPlace().getId())
                .ReviewId(review.getId())
                .rating(review.getRating())
                .reviewContent(review.getContent())
                .reviewImageList(reviewImageList)
                .build();
    }

    public static ReviewResponseDTO.GetReviewListResponseDTO toGetReviewListResponseDTO(
            Page<Review> reviewPage, List<ReviewResponseDTO.GetReviewResponseDTO> reviewResponseDTOS, Double totalRating) {
        return ReviewResponseDTO.GetReviewListResponseDTO.builder()
                .listSize(reviewPage.getSize())
                .totalPage(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .isFirst(reviewPage.isFirst())
                .isLast(reviewPage.isLast())
                .totalReviewRating(totalRating)
                .getReviewList(reviewResponseDTOS)
                .build();
    }
}