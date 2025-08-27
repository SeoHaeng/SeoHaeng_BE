package com.seohaeng.backend.domain.place.controller;

import com.seohaeng.backend.domain.place.dto.PlaceSearchDTO;
import com.seohaeng.backend.domain.place.service.PlaceSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;

    @Operation(summary = "키워드 기반 장소 검색", description = """
        키워드 기반 검색 API입니다.
        - keyword : 검색할 키워드 (예: '서점', '카페')
        - minLat, minLng, maxLat, maxLng : 현재 뷰포트(지도 화면) 좌표값
        """
    )
    @GetMapping("/search")
    public List<PlaceSearchDTO> searchPlaces(
            @RequestParam String keyword,
            @RequestParam double minLat,
            @RequestParam double minLng,
            @RequestParam double maxLat,
            @RequestParam double maxLng
    ) {
        return placeSearchService.searchPlaces(keyword, minLat, minLng, maxLat, maxLng);
    }

}
