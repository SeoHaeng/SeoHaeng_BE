package com.seohaeng.backend.domain.travelCourse.controller;

import com.seohaeng.backend.domain.travelCourse.dto.TravelCourseRequestDTO;
import com.seohaeng.backend.domain.travelCourse.dto.TravelCourseResponseDTO;
import com.seohaeng.backend.domain.travelCourse.service.TravelCourseCommandService;
import com.seohaeng.backend.domain.travelCourse.service.TravelCourseQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/travel-courses")
public class TravelCourseController {

    private final TravelCourseCommandService travelCourseCommandService;
    private final TravelCourseQueryService travelCourseQueryService;

    @Operation(
            summary = "여행 일정 생성 API",
            description = """
    여행 일정을 생성하는 API입니다.
    여행의 시작/종료일, 여행 일정 제목, 여행할 강원도 지역들의 ID 리스트와
    날짜별 장소 스케줄 리스트를 포함한 데이터를 요청 본문으로 전달해야 합니다.

    - `startDate` (날짜, 문자열): 여행 시작 날짜 (예: "2025-07-24")
    - `endDate` (날짜, 문자열): 여행 종료 날짜 (예: "2025-07-25")
    - `travelCourseTitle` (문자열): 여행 제목 (예: "강릉 1박 2일")
    - `regionIdList` (숫자 배열): 여행 경로에 포함된 지역 ID 목록 (예: [1, 2])
    - `travelCourseScheduleList` (객체 배열): 날짜별 방문할 장소와 순서 목록
    - 'isPublic' (true/false): 공개 여부

    `travelCourseScheduleList` 내부 필드:
    - `day` (날짜, 문자열): 방문 날짜 (예: "2025-07-24")
    - `orderInday` (숫자): 하루 방문 순서 (1부터 시작)
    - `placeId` (숫자): 방문할 시설의 ID
"""
    )
    @PostMapping
    public ApiResponse<String> createTravelCourse (
            @AuthUser Long userId,
            @RequestBody TravelCourseRequestDTO.CreateTravelCourseDTO request) {
        Long travelCourseId = travelCourseCommandService.createTravelCourse(userId, request);
        return ApiResponse.onSuccess("여행 일정 생성이 완료되었습니다. " + "TravelCourse ID: " + travelCourseId);
    }

    @Operation(
            summary = "여행 일정 개별 상세 조회 API",
            description = "개별 여행 일정을 조회하는 API입니다. 조회하고자 하는 여행 일정의 Id를 넘겨주세요."
    )
    @GetMapping("/{TravelCourseId}")
    public ApiResponse<TravelCourseResponseDTO.GetTravelCourseResponseDTO> getTravelCourse (
            @PathVariable Long TravelCourseId) {
        TravelCourseResponseDTO.GetTravelCourseResponseDTO result
                = travelCourseQueryService.getTravelCourse(TravelCourseId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "나의 여행 일정 전체 조회 API",
            description = "본인이 생성한 여행 일정들을 전체 조회하는 API입니다."
    )
    @GetMapping("/mine")
    public ApiResponse<List<TravelCourseResponseDTO.GetTravelCourseListItemDTO>> getMyTravelCourse (
            @AuthUser Long userId) {
        List<TravelCourseResponseDTO.GetTravelCourseListItemDTO> result = travelCourseQueryService.getTravelCourseList(userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "여행 일정 삭제 API",
            description = "여행 일정을 삭제합니다. 본인의 여행 일정에만 삭제 권한이 있습니다. 삭제하고자 하는 여행일정의 Id를 전달해주세요."
    )
    @DeleteMapping("/{TravelCourseId}")
    public ApiResponse<String> deleteTravelCourse (
            @AuthUser Long userId,
            @PathVariable Long TravelCourseId) {
        travelCourseCommandService.deleteTravelCourse(userId, TravelCourseId);
        return ApiResponse.onSuccess("여행 일정 삭제가 완료되었습니다.");
    }

    @Operation(
            summary = "마지막 강원도 여행 날짜 조회 API",
            description = "마지막 강원도 여행 날짜를 조회하는 API입니다."
    )
    @GetMapping("/last-visit")
    public ApiResponse<TravelCourseResponseDTO.LastVisitDTO> getLastVisitDay(@AuthUser Long userId) {
        TravelCourseResponseDTO.LastVisitDTO result = travelCourseQueryService.getLastVisit(userId);
        return ApiResponse.onSuccess(result);
    }
}