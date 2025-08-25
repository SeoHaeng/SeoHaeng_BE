package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.converter.PlaceConverter;
import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.BookChallengeEvent;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.repository.BookChallengeEventRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceRepository placeRepository;
    private final BookChallengeEventRepository bookChallengeEventRepository;

    public PlaceResponseDTO.placeListDto findBookChallengePlaces(Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page-1, size,  Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookChallengeEvent> bookChallengeEvents = bookChallengeEventRepository.findAll(pageRequest);

        List<Place> places = bookChallengeEvents.getContent().stream()
                .map(BookChallengeEvent::getPlace)
                .collect(Collectors.toList());

        List<PlaceResponseDTO.placeDto> placeDtoList = places.stream()
                .map(PlaceConverter::toplaceDto).collect(Collectors.toList());

        return PlaceConverter.toplaceListDto(bookChallengeEvents, placeDtoList);
    }

    public List<PlaceResponseDTO.TodayPlaceResponse> getTodayPlace() {
        return List.of(PlaceType.BOOKSTORE, PlaceType.TOURIST_SPOT, PlaceType.FESTIVAL)
                .stream()
                .map(placeType -> placeRepository.findRandomByPlaceType(placeType.name()))
                .flatMap(Optional::stream)
                .map(place -> {
                    String overview = getOverviewByPlaceType(place);
                    return PlaceConverter.toTodayPlaceResponse(place, overview);
                })
                .collect(Collectors.toList());
    }
    
    private String getOverviewByPlaceType(Place place) {
        return switch (place.getPlaceType()) {
            case BOOKSTORE -> place.getBookStoreAttribute().getOverview();
            case TOURIST_SPOT -> place.getTouristSpotAttribute().getOverview();
            case FESTIVAL -> place.getFestivalAttribute().getOverview();
            default -> null;
        };
    }
}