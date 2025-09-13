package com.seohaeng.backend.domain.common.controller;

import com.seohaeng.backend.domain.common.dto.CommonResponseDTO;
import com.seohaeng.backend.domain.common.service.CommonQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonQueryService commonQueryService;

    @Operation(
            summary = "책 검색 API",
            description = """
        네이버 책 검색 API를 중계하는 엔드포인트 입니다.
        Parameter:
        - `query`: 검색어
        - `display`: 한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
        - `start`: 검색 시작 위치(기본값: 1, 최댓값: 1000)
        - `sort`: 검색 결과 정렬 방법 ( sim: 정확도순으로 내림차순 정렬(기본값) / date: 출간일순으로 내림차순 정렬)
    """)
    @GetMapping("/books")
    public ApiResponse<CommonResponseDTO.bookSearchResultListDTO> searchBooks (@RequestParam @NotBlank String query,
                                                                               @RequestParam(required = false)  @Min(1) @Max(100) Integer display,
                                                                               @RequestParam(required = false)  @Min(1) @Max(1000) Integer start,
                                                                               @RequestParam(required = false)  @Pattern(regexp = "sim|date") String sort){
        CommonResponseDTO.bookSearchResultListDTO result = commonQueryService.bookSearch(query, display, start, sort);
        return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "Server Health Check API")
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.onSuccess("OK");
    }
}