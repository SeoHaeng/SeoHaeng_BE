package com.seohaeng.backend.domain.place.controller;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.service.BookStoreAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceMarkerController {

    private final BookStoreAttributeService service;

    @Operation(
            summary = "북스테이 마커 조회",
            description = """
        지도 위의 북스테이 장소를 마커 형태로 조회합니다.
        - 각 장소의 좌표, 이름을 반환합니다.
        """
    )
    @GetMapping("/markers/bookstays")
    public List<PlaceMarkerDTO> getBookStayMarkers() {
        return service.getBookStayMarkers();
    }

    @Operation(
            summary = "북카페 마커 조회",
            description = """
        지도 위의 북카페 장소를 마커 형태로 조회합니다.
        - 각 장소의 좌표, 이름을 반환합니다.
        """
    )
    @GetMapping("/markers/bookcafes")
    public List<PlaceMarkerDTO> getBookCafeMarkers() {
        return service.getBookCafeMarkers();
    }

    @Operation(
            summary = "독립서점 마커 조회",
            description = """
        지도 위의 독립서점 장소를 마커 형태로 조회합니다.
        - 각 장소의 좌표, 이름을 반환합니다.
        """
    )
    @GetMapping("/markers/bookstores")
    public List<PlaceMarkerDTO> getIndieBookStoreMarkers() {
        return service.getBookstoreMarkers();
    }

    @Operation(
            summary = "공간책갈피 마커 조회",
            description = """
        지도 위의 공간책갈피 장소를 마커 형태로 조회합니다.
        - 각 장소의 좌표, 이름을 반환합니다.
        """
    )
    @GetMapping("/markers/spacebookmarks")
    public List<PlaceMarkerDTO> getSpaceBookmarkMarkers() {
        return service.getSpaceBookmarkMarkers();
    }

}
