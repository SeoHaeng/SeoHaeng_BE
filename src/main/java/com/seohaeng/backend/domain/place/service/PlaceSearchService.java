package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceSearchDTO;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.util.MarkerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {

    private final PlaceRepository placeRepository;

    public List<PlaceSearchDTO> searchPlaces(String keyword, double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                placeRepository.findByNameContaining(keyword),
                Place::getLatitude,
                Place::getLongitude,
                place -> new PlaceSearchDTO(place.getId(), place.getName(), place.getPlaceType().name(), place.getAddress()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

}
