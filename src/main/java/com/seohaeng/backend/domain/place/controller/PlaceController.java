package com.seohaeng.backend.domain.place.controller;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.service.PlaceCommandService;
import com.seohaeng.backend.domain.place.service.PlaceQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceQueryService placeQueryService;
    private final PlaceCommandService placeCommandService;

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
    public ApiResponse<PlaceResponseDTO.BookStoreListDto> getBookChallengePlaces(
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size
    ) {
        return ApiResponse.onSuccess(placeQueryService.findBookChallengePlaces(page,size));
    }

    @Operation(
            summary = "장소 찜하기 토글 API",
            description = """
        특정 공간책갈피에 대해 장소 찜하기 상태를 토글하고, 현재 찜 수를 반환합니다.
        - 장소 찜하기를 할 공간책갈피 ID를 전달해주세요.
        - 해당 장소를 아직 찜하지 않았다면 새로 찜하며,
        - 이미 찜한 상태라면 기존 찜을 취소합니다.
        """
    )
    @PostMapping("/{placeId}/book-marks")
    public ApiResponse<PlaceResponseDTO.PlaceBookmarkToggleResponse> toggleBookMarkPlace(
            @AuthUser Long userId, @PathVariable("placeId") Long placeId) {
        PlaceResponseDTO.PlaceBookmarkToggleResponse result =
                placeCommandService.toggleBookMarkPlace(userId, placeId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "오늘의 추천 강원도 조회 API",
            description = """
        메인 홈에 오늘의 추천 강원도 데이터를 조회합니다. 랜덤으로 독립서점, 축제, 관광지 데이터가 하나씩 조회됩니다.
    """
    )
    @GetMapping("/today")
    public ApiResponse<List<PlaceResponseDTO.TodayPlaceResponse>> getBookChallengePlaces() {
        return ApiResponse.onSuccess(placeQueryService.getTodayPlace());
    }


    @Operation(
            summary = "현재 진행 중인 축제 조회 API",
            description = """
        현재 진행 중인 강원도 축제를 조회합니다.
        """
    )
    @GetMapping("/festival")
    public ApiResponse<List<PlaceResponseDTO.OngoingFestivalResponse>> getOngoingFestival() {
        return ApiResponse.onSuccess(placeQueryService.getOngoingFestival());
    }
}
