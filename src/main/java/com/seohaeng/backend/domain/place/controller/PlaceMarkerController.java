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
            summary = "지도 위 북스테이만 조회 API")
    @GetMapping("/markers/bookstays")
    public List<PlaceMarkerDTO> getBookStayMarkers() {
        return service.getBookStayMarkers();
    }

}
