package com.seohaeng.backend.domain.place.controller;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.service.PlaceQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceQueryService placeQueryService;

    @Operation(
            summary = "북챌린지 서점 조회 API",
            description = """
        북챌린지 이벤트가 진행 중인 서점들의 목록을 페이징 처리하여 조회합니다.
        Parameter:
        - `page`: 페이지 번호 (1부터 시작)
        - `size`: 한 페이지당 게시글 개수 (기본값: 10)
    """
    )
    @GetMapping("/book-challenges")
    public ApiResponse<PlaceResponseDTO.placeListDto> getBookChallengePlaces(
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size
    ) {
        return ApiResponse.onSuccess(placeQueryService.findBookChallengePlaces(page,size));
    }

}
