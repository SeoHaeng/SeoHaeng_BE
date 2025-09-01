package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.RestaurantAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.TouristSpotAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceMarkerService {

    private final FestivalAttributeRepository festivalRepository;
    private final RestaurantAttributeRepository restaurantRepository;
    private final TouristSpotAttributeRepository touristSpotRepository;

    public List<PlaceMarkerDTO> getTouristSpotMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return touristSpotRepository.findAll().stream()
                .filter(attr -> attr.getPlace().getLatitude() != null && attr.getPlace().getLongitude() != null)
                .filter(attr -> isWithinViewport(attr.getPlace().getLatitude(), attr.getPlace().getLongitude(),
                        minLat, minLng, maxLat, maxLng))
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }

    public List<PlaceMarkerDTO> getFestivalMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return festivalRepository.findOngoingFestivals(LocalDate.now()).stream()
                .filter(attr -> attr.getPlace().getLatitude() != null && attr.getPlace().getLongitude() != null)
                .filter(attr -> isWithinViewport(attr.getPlace().getLatitude(), attr.getPlace().getLongitude(),
                        minLat, minLng, maxLat, maxLng))
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }

    public List<PlaceMarkerDTO> getRestaurantMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return restaurantRepository.findAll().stream()
                .filter(attr -> attr.getPlace().getLatitude() != null && attr.getPlace().getLongitude() != null)
                .filter(attr -> isWithinViewport(attr.getPlace().getLatitude(), attr.getPlace().getLongitude(),
                        minLat, minLng, maxLat, maxLng))
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }

    private boolean isWithinViewport(double lat, double lng, double minLat, double minLng, double maxLat, double maxLng) {
        return lat >= minLat && lat <= maxLat && lng >= minLng && lng <= maxLng;
    }

}