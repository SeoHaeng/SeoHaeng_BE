package com.seohaeng.backend.domain.readingSpot.controller;

import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.service.ReadingSpotCommandService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.apiPayload.code.status.SuccessStatus;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
}
