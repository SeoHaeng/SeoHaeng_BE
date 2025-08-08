package com.seohaeng.backend.domain.review.service;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.review.converter.ReviewConverter;
import com.seohaeng.backend.domain.review.dto.ReviewResponseDTO;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.review.entity.ReviewImage;
import com.seohaeng.backend.domain.review.repository.ReviewImageRepository;
import com.seohaeng.backend.domain.review.repository.ReviewRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    public ReviewResponseDTO.GetReviewListResponseDTO getReviewList(Integer page, Integer size, Long placeId){

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));

        Double averageRating = Optional.ofNullable(reviewRepository.getAverageRatingByPlace(place))
                .orElse(0.0);

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.findAllByPlace(place, pageRequest);

        List<ReviewResponseDTO.GetReviewResponseDTO> getReviewList = reviewPage.getContent().stream()
                .map(review -> {
                    List<String> reviewImageList = review.getReviewImageList().stream()
                            .map(ReviewImage::getImageUrl)
                            .toList();
                    return ReviewConverter.toGetReviewResponseDTO(review, reviewImageList);
                })
                .toList();
        return ReviewConverter.toGetReviewListResponseDTO(reviewPage,getReviewList,averageRating);
    }
}