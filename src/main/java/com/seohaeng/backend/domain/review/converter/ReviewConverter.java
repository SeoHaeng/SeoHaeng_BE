package com.seohaeng.backend.domain.review.converter;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.review.dto.ReviewRequestDTO;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.user.entity.User;

public class ReviewConverter {

    public static Review toReview(
            ReviewRequestDTO.CreateReviewRequestDTO reviewRequestDTO, User user, Place place) {
        return Review.builder()
                .place(place)
                .content(reviewRequestDTO.getContent())
                .rating(reviewRequestDTO.getRating())
                .visitedDate(reviewRequestDTO.getVisitedDate())
                .user(user)
                .build();
    }
}