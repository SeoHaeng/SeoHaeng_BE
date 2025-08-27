package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.converter.PlaceConverter;
import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.BookChallengeEvent;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.repository.BookChallengeEventRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.BookStoreAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceRepository placeRepository;
    private final BookStoreAttributeRepository bookStoreAttributeRepository;
    private final FestivalAttributeRepository festivalAttributeRepository;

    // 북챌린지 서점 조회
    public PlaceResponseDTO.BookStoreListDto findBookChallengePlaces(Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page-1, size,  Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<BookStoreAttribute> bookStoreAttributes
                = bookStoreAttributeRepository.findAllByBookChallengeStatusTrue(pageRequest);

        List<Place> places = bookStoreAttributes.getContent().stream()
                .map(BookStoreAttribute::getPlace)
                .collect(Collectors.toList());

        List<PlaceResponseDTO.BookStoreDto> placeDtoList = places.stream()
                .map(PlaceConverter::toBookStoreDto).collect(Collectors.toList());

        return PlaceConverter.toBookStoreListDto(bookStoreAttributes, placeDtoList);
    }

    // 오늘의 추천 강원도 조회
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

    // 진행 중인 축제 조회
    public List<PlaceResponseDTO.OngoingFestivalResponse> getOngoingFestival() {
        List<FestivalAttribute> festivalAttributes = festivalAttributeRepository.findOngoingFestivals(LocalDate.now());
        
        return festivalAttributes.stream()
                .map(festivalAttribute
                        -> PlaceConverter.toOngoingFestivalResponse(festivalAttribute.getPlace(), festivalAttribute))
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