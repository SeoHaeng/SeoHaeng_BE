package com.seohaeng.backend.domain.readingSpot.controller;

import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.service.ReadingSpotCommandService;
import com.seohaeng.backend.domain.readingSpot.service.ReadingSpotQueryService;
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
@RequestMapping("/api/v1/reading-sopt")
@RequiredArgsConstructor
public class ReadingSpotController {

    private final ReadingSpotCommandService readingSpotCommandService;
    private final ReadingSpotQueryService readingSpotQueryService;

    @Operation(
            summary = "공간책갈피 생성 API",
            description = """
    공간책갈피를 생성합니다.
    - 최대 10장의 이미지를 함께 업로드할 수 있습니다.
    """
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReadingSpotResponseDTO.CreateReadingSpotResponseDTO> registerReadingSpot(
            @AuthUser Long userId,
            @RequestPart("request") @Valid ReadingSpotRequestDTO.ReadingSpotCreateRequestDTO request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {
        if (images != null && images.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10장까지 업로드할 수 있습니다.");
        }
        ReadingSpotResponseDTO.CreateReadingSpotResponseDTO result
                = readingSpotCommandService.createReadingSpot(userId, images, request);
        return ApiResponse.of(SuccessStatus.READING_SPOT_CREATE_SUCCESS, result);
    }

    @Operation(
            summary = "공간책갈피 상세 조회 API",
            description = """
    특정 공간책갈피를 상세 조회합니다.
    - ReadingSpotId를 통해 상세 조회할 공간책갈피를 지정합니다.
    """
    )
    @GetMapping("/{ReadingSpotId}")
    public ApiResponse<ReadingSpotResponseDTO.GetReadingSpotResponseDTO> getReadingSpot(
            @PathVariable("ReadingSpotId") Long readingSpotId) {
        ReadingSpotResponseDTO.GetReadingSpotResponseDTO result = readingSpotQueryService.getReadingSpot(readingSpotId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "공간책갈피 댓글 작성 API",
            description = """
    특정 공간책갈피에 댓글을 작성합니다.
    - ReadingSpotId를 통해 댓글을 달 공간책갈피를 지정합니다.
    """
    )
    @PostMapping("/{ReadingSpotId}/comments")
    public ApiResponse<ReadingSpotResponseDTO.CreateReadingSpotCommentResponseDTO> createReadingSpotComment(
            @AuthUser Long userId,
            @PathVariable("ReadingSpotId") Long readingSpotId,
            @RequestBody @Valid ReadingSpotRequestDTO.ReadingSpotCommentCreateRequestDTO request) {
        ReadingSpotResponseDTO.CreateReadingSpotCommentResponseDTO result
                = readingSpotCommandService.createReadingSpotComment(userId, readingSpotId, request);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "공간책갈피 댓글 리스트 조회 API",
            description = """
    특정 공간책갈피의 댓글 목록을 페이징 처리하여 조회합니다. 기본으로 최신순 조회됩니다.
    - ReadingSpotId를 통해 댓글 목록을 조회할 공간책갈피를 지정합니다.
    - Parameter:
       - `page`: 페이지 번호 (1부터 시작)
       - `size`: 한 페이지당 게시글 개수 (기본값: 20)
    """
    )
    @GetMapping("/{ReadingSpotId}/comments")
    public ApiResponse<ReadingSpotResponseDTO.GetReadingSpotCommentListResponseDTO> getReadingSpotComments(
            @PathVariable("ReadingSpotId") Long readingSpotId,
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size) {
        ReadingSpotResponseDTO.GetReadingSpotCommentListResponseDTO result
                = readingSpotQueryService.getReadingSpotCommentList(readingSpotId, page, size);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "공간책갈피 좋아요 토글 API",
            description = "특정 공간책갈피에 좋아요를 추가하거나 취소하고, 현재 좋아요 수를 반환합니다." +
                    "누르면 좋아요가 없으면 +1, 이미 눌러져 있으면 취소되어 좋아요 수가 -1 됩니다. 좋아요를 토글할 공간책갈피 ID를 전달해주세요."
    )
    @PostMapping("/{ReadingSpotId}/likes")
    public ApiResponse<ReadingSpotResponseDTO.GetReadingSpotLikeInfoDTO> toggleLikeReadingSpot(
            @AuthUser Long userId, @PathVariable("ReadingSpotId") Long readingSpotId) {
        ReadingSpotResponseDTO.GetReadingSpotLikeInfoDTO result =
                readingSpotCommandService.toggleReadingSpotLikes(userId, readingSpotId);
        return ApiResponse.of(SuccessStatus.READING_SPOT_LIKE_TOGGLED,result);
    }

    @Operation(
            summary = "공간책갈피 스크랩(저장) 토글 API",
            description = "특정 공간책갈피를 스크랩하거나 스크랩을 취소하고, 현재 스크랩 수를 반환합니다. 스크랩을 토글할 공간책갈피 ID를 전달해주세요."
            +"스크랩이 되어있지 않은 공간책갈피라면 새로 스크랩을 하며, 이미 스크랩이 되어있다면 기존의 스크랩이 취소됩니다."
    )
    @PostMapping("/{ReadingSpotId}/scraps")
    public ApiResponse<ReadingSpotResponseDTO.GetReadingSpotScrapInfoDTO> toggleScrapReadingSpot(
            @AuthUser Long userId, @PathVariable("ReadingSpotId") Long readingSpotId) {
        ReadingSpotResponseDTO.GetReadingSpotScrapInfoDTO result =
                readingSpotCommandService.toggleReadingSpotScraps(userId, readingSpotId);
        return ApiResponse.of(SuccessStatus.READING_SPOT_SCRAP_TOGGLED,result);
    }

    @Operation(
            summary = "내가 스크랩한(저장) 공간책갈피 리스트 조회 API",
            description = """
    내가 스크랩한(저장한) 공간책갈피 목록을 조회합니다. 기본 최신순으로 조회됩니다.
    - Parameter:
      - `page`: 페이지 번호 (1부터 시작)
      - `size`: 한 페이지당 게시글 개수 (기본값: 10)
    """
    )
    @GetMapping("/scraps/my")
    public ApiResponse<ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO> getMyScrapReadingSpot(
            @AuthUser Long userId,
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size) {
        ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO result
                = readingSpotQueryService.getMyScrapReadingSpotListResponseDTO(userId, page, size);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "내가 등록한 공간책갈피 리스트 조회 API",
            description = """
    내가 등록한 공간책갈피 목록을 조회합니다. 기본 최신순으로 조회됩니다.
    - Parameter:
      - `page`: 페이지 번호 (1부터 시작)
      - `size`: 한 페이지당 게시글 개수 (기본값: 10)
    """
    )
    @GetMapping("/my")
    public ApiResponse<ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO> getMyReadingSpot(
            @AuthUser Long userId,
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size) {
        ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO result
                = readingSpotQueryService.getMyReadingSpotListResponseDTO(userId, page, size);
        return ApiResponse.onSuccess(result);
    }
}