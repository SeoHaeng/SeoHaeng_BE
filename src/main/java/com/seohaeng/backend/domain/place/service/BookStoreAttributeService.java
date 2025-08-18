package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.place.repository.BookStoreAttributeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookStoreAttributeService {

    private final BookStoreAttributeRepository repository;

    public BookStoreAttributeService(BookStoreAttributeRepository repository) {
        this.repository = repository;
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

//    public List<PlaceMarkerDTO> getIndieBookStoreMarkers() {
//        return repository.findAllIndiePublication().stream()
//                .map(attr -> new PlaceMarkerDTO(
//                        attr.getPlace().getId(),
//                        attr.getPlace().getName(),
//                        attr.getPlace().getLatitude(),
//                        attr.getPlace().getLongitude()
//                ))
//                .toList();
//    }

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

    public List<PlaceMarkerDTO> getSpaceBookmarkMarkers() {
        return repository.findAllSpaceBookmark().stream()
                .map(attr -> new PlaceMarkerDTO(
                        attr.getPlace().getId(),
                        attr.getPlace().getName(),
                        attr.getPlace().getLatitude(),
                        attr.getPlace().getLongitude()
                ))
                .toList();
    }


}
