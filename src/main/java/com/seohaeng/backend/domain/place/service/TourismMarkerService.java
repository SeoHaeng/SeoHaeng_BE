package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.util.MarkerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TourismMarkerService {

    private final PlaceRepository placeRepository;

    public List<PlaceMarkerDTO> getTouristSpotMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                placeRepository.findByPlaceType(PlaceType.TOURIST_SPOT),
                place -> place.getLatitude(),
                place -> place.getLongitude(),
                place -> new PlaceMarkerDTO(place.getId(), place.getName(), place.getLatitude(), place.getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getFestivalMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                placeRepository.findOngoingFestivals(LocalDate.now()),
                place -> place.getLatitude(),
                place -> place.getLongitude(),
                place -> new PlaceMarkerDTO(place.getId(), place.getName(), place.getLatitude(), place.getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getRestaurantMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                placeRepository.findByPlaceType(PlaceType.RESTAURANT),
                place -> place.getLatitude(),
                place -> place.getLongitude(),
                place -> new PlaceMarkerDTO(place.getId(), place.getName(), place.getLatitude(), place.getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

}
