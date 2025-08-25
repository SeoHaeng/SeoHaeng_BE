package com.seohaeng.backend.domain.place.controller;

import com.seohaeng.backend.domain.place.dto.PlaceInfoDTO;
import com.seohaeng.backend.domain.place.service.PlaceInfoService;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceInfoController {

    private final PlaceInfoService placeInfoService;

    @Operation(
            summary = "마커 클릭 시 하단 모달(장소 상세 정보) 조회",
            description = """
        마커 클릭 시 하단 모달의 장소 상세 정보를 조회합니다.
        - 현 위치 좌표, 유저/장소 아이디를 전달해 주세요.
        - 현 위치와 장소 간 거리, 찜한 장소 여부,
        - 장소의 리뷰 정보, 주소를 반환합니다.
        """
    )
    @GetMapping("/{placeId}")
    public PlaceInfoDTO getPlaceInfo(
            @PathVariable Long placeId,
            @RequestParam Double currentLat,
            @RequestParam Double currentLng,
            @RequestParam(required = false) @AuthUser Long userId
    ) {
        return placeInfoService.getPlaceInfo(placeId, userId, currentLat, currentLng);
    }

}
