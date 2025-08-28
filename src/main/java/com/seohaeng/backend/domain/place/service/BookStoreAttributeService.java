package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.repository.attribute.BookStoreAttributeRepository;
import com.seohaeng.backend.domain.readingSpot.repository.ReadingSpotRepository;
import org.springframework.data.domain.Pageable;
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

    public List<PlaceMarkerDTO> getBookStayMarkers() {
        return repository.findAllBookStay().stream()
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }

    public List<PlaceMarkerDTO> getBookCafeMarkers() {
        return repository.findAllBookCafe().stream()
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }

    public List<PlaceMarkerDTO> getBookstoreMarkers() {
        return repository.findAllBookstore().stream()
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }

    public List<PlaceMarkerDTO> getReadingSpotMarkers() {
        return readingSpotRepository.findAllByOpenedTrue(Pageable.unpaged())
                .stream()
                .map(r -> new PlaceMarkerDTO(
                        r.getId(),
                        r.getTitle(),
                        r.getLatitude(),
                        r.getLongitude()
                ))
                .toList();
    }

}
