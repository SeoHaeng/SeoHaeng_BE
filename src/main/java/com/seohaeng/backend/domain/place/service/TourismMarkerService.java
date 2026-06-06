package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TourismMarkerService {

    private final PlaceRepository placeRepository;
    private static final Pageable MARKER_LIMIT = PageRequest.of(0, 100);

    public List<PlaceMarkerDTO> getTouristSpotMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return placeRepository.findMarkerDTOsByPlaceTypeAndBounds(
                PlaceType.TOURIST_SPOT, minLat, maxLat, minLng, maxLng, MARKER_LIMIT
        );
    }

    public List<PlaceMarkerDTO> getFestivalMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return placeRepository.findOngoingFestivalMarkerDTOsByBounds(
                LocalDate.now(), minLat, maxLat, minLng, maxLng
        );
    }

    public List<PlaceMarkerDTO> getRestaurantMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return placeRepository.findMarkerDTOsByPlaceTypeAndBounds(
                PlaceType.RESTAURANT, minLat, maxLat, minLng, maxLng, MARKER_LIMIT
        );
    }
}