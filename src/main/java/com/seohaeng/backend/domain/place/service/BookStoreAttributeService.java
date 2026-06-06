package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.repository.attribute.BookStoreAttributeRepository;
import com.seohaeng.backend.domain.readingSpot.repository.ReadingSpotRepository;
import com.seohaeng.backend.domain.place.util.MarkerUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookStoreAttributeService {

    private final BookStoreAttributeRepository repository;
    private final ReadingSpotRepository readingSpotRepository;

    public BookStoreAttributeService(BookStoreAttributeRepository repository, ReadingSpotRepository readingSpotRepository) {
        this.repository = repository;
        this.readingSpotRepository = readingSpotRepository;
    }

    public List<PlaceMarkerDTO> getBookStayMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                repository.findAllBookStay(),
                attr -> attr.getPlace().getLatitude(),
                attr -> attr.getPlace().getLongitude(),
                attr -> new PlaceMarkerDTO(attr.getPlace().getId(), attr.getPlace().getName(),
                        attr.getPlace().getLatitude(), attr.getPlace().getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getBookCafeMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                repository.findAllBookCafe(),
                attr -> attr.getPlace().getLatitude(),
                attr -> attr.getPlace().getLongitude(),
                attr -> new PlaceMarkerDTO(attr.getPlace().getId(), attr.getPlace().getName(),
                        attr.getPlace().getLatitude(), attr.getPlace().getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getBookstoreMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return MarkerUtils.filterAndConvert(
                repository.findAllBookstore(),
                attr -> attr.getPlace().getLatitude(),
                attr -> attr.getPlace().getLongitude(),
                attr -> new PlaceMarkerDTO(attr.getPlace().getId(), attr.getPlace().getName(),
                        attr.getPlace().getLatitude(), attr.getPlace().getLongitude()),
                minLat, minLng, maxLat, maxLng,
                5
        );
    }

    public List<PlaceMarkerDTO> getReadingSpotMarkers(double minLat, double minLng, double maxLat, double maxLng) {
        return readingSpotRepository.findMarkerDTOsByBounds(minLat, maxLat, minLng, maxLng, PageRequest.of(0, 5));
    }

}
