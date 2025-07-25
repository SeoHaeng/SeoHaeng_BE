package com.seohaeng.backend.domain.travelCourse.controller;

import com.seohaeng.backend.domain.travelCourse.dto.StampResponseDTO;
import com.seohaeng.backend.domain.travelCourse.service.StampQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stamps")
public class StampController {

    private final StampQueryService stampQueryService;

    @Operation(
            summary = "나의 스탬프(발자국) 현황 조회 API",
            description = "기억 마당 페이지의 스탬프(발자국) 현황을 조회하는 API입니다. 지금까지 모은 발자국의 갯수와 각 지역별의 방문 여부를 조회합니다."
    )
    @GetMapping
    public ApiResponse<StampResponseDTO.GetMyStampResponseDTO> getStamps(@AuthUser Long userId){
        StampResponseDTO.GetMyStampResponseDTO result = stampQueryService.getMyStamp(userId);
        return ApiResponse.onSuccess(result);
    }
}