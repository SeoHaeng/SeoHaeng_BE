package com.seohaeng.backend.domain.review.controller;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.review.dto.ReviewRequestDTO;
import com.seohaeng.backend.domain.review.dto.ReviewResponseDTO;
import com.seohaeng.backend.domain.review.service.ReviewCommandService;
import com.seohaeng.backend.domain.review.service.ReviewQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.apiPayload.code.status.SuccessStatus;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @Operation(
            summary = "리뷰 작성 API",
            description = """
    특정 장소에 대한 리뷰를 작성합니다.

    - placeId를 통해 리뷰할 장소를 지정합니다.
    - 텍스트 리뷰 내용은 request 필드로 전송되며, JSON 형식의 리뷰 정보가 포함되어야 합니다.
    - 최대 10장의 리뷰 이미지를 함께 업로드할 수 있습니다.
    """
    )
    @PostMapping(value = "/{placeId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponseDTO.CreateReviewResponseDTO> registerReview (
            @AuthUser Long userId,
            @PathVariable("placeId") Long placeId,
            @RequestPart("request") @Valid ReviewRequestDTO.CreateReviewRequestDTO request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images){
        if (images != null && images.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10장까지 업로드할 수 있습니다.");
        }
        ReviewResponseDTO.CreateReviewResponseDTO result = reviewCommandService.createReview(request, userId, placeId, images);
        return ApiResponse.of(SuccessStatus.REVIEW_CREATE_SUCCESS, result);
    }

    @Operation(
            summary = "리뷰 목록 조회 API",
            description = """
    특정 장소에 대한 리뷰 목록을 조회합니다 API"
    - placeId를 통해 리뷰 목록을 조회할 장소를 지정합니다.
    """
    )
    @GetMapping(value = "/{placeId}")
    public ApiResponse<ReviewResponseDTO.GetReviewListResponseDTO> getReviews(
            @PathVariable("placeId") Long placeId,
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1)Integer size
    ){
        return ApiResponse.onSuccess(reviewQueryService.getReviewList(page, size, placeId));
    }
}