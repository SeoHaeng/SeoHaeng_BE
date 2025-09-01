package com.seohaeng.backend.domain.place.controller;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.service.PlaceMarkerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class TourismMarkerController {

    private final PlaceMarkerService placeMarkerService;

    @Operation(
            summary = "관광지 마커 조회",
            description = """
    지도 위의 가볼만한 관광지 장소를 마커 형태로 조회합니다.
    - 각 장소의 좌표, 이름을 반환합니다.
    """
    )
    @GetMapping("/markers/touristspots")
    public List<PlaceMarkerDTO> getTouristSpotMarkers(
            @RequestParam double minLat,
            @RequestParam double minLng,
            @RequestParam double maxLat,
            @RequestParam double maxLng
    ) {
        return placeMarkerService.getTouristSpotMarkers(minLat, minLng, maxLat, maxLng);
    }

    @Operation(
            summary = "핫플 마커 조회",
            description = """
    지도 위의 축제(핫플) 장소를 마커 형태로 조회합니다.
    - 각 장소의 좌표, 이름을 반환합니다.
    """
    )
    @GetMapping("/markers/festivals")
    public List<PlaceMarkerDTO> getFestivalMarkers(
            @RequestParam double minLat,
            @RequestParam double minLng,
            @RequestParam double maxLat,
            @RequestParam double maxLng
    ) {
        return placeMarkerService.getFestivalMarkers(minLat, minLng, maxLat, maxLng);
    }

    @Operation(
            summary = "맛집 마커 조회",
            description = """
    지도 위의 맛집 장소를 마커 형태로 조회합니다.
    - 각 장소의 좌표, 이름을 반환합니다.
    """
    )
    @GetMapping("/markers/restaurants")
    public List<PlaceMarkerDTO> getRestaurantMarkers(
            @RequestParam double minLat,
            @RequestParam double minLng,
            @RequestParam double maxLat,
            @RequestParam double maxLng
    ) {
        return placeMarkerService.getRestaurantMarkers(minLat, minLng, maxLat, maxLng);
    }

}