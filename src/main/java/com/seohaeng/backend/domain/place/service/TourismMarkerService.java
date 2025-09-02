package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.RestaurantAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.TouristSpotAttributeRepository;
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

    private final FestivalAttributeRepository festivalRepository;
    private final RestaurantAttributeRepository restaurantRepository;
    private final TouristSpotAttributeRepository touristSpotRepository;

    public List<PlaceMarkerDTO> getTouristSpotMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                touristSpotRepository.findAll(),
                attr -> attr.getPlace().getLatitude(),
                attr -> attr.getPlace().getLongitude(),
                attr -> new PlaceMarkerDTO(attr.getPlace().getId(), attr.getPlace().getName(),
                        attr.getPlace().getLatitude(), attr.getPlace().getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getFestivalMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                festivalRepository.findOngoingFestivals(LocalDate.now()),
                attr -> attr.getPlace().getLatitude(),
                attr -> attr.getPlace().getLongitude(),
                attr -> new PlaceMarkerDTO(attr.getPlace().getId(), attr.getPlace().getName(),
                        attr.getPlace().getLatitude(), attr.getPlace().getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getRestaurantMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                restaurantRepository.findAll(),
                attr -> attr.getPlace().getLatitude(),
                attr -> attr.getPlace().getLongitude(),
                attr -> new PlaceMarkerDTO(attr.getPlace().getId(), attr.getPlace().getName(),
                        attr.getPlace().getLatitude(), attr.getPlace().getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

}
