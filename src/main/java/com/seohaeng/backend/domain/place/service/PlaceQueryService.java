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
import com.seohaeng.backend.domain.place.repository.SavedPlaceRepository;
import com.seohaeng.backend.domain.review.repository.ReviewRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
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
    private final ReviewRepository reviewRepository;
    private final SavedPlaceRepository savedPlaceRepository;
    private final UserRepository userRepository;

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

    // 장소 상세 조회
    public PlaceResponseDTO.PlaceDatail getPlaceDetail(Long placeId, Long userId) {
        Place place = placeRepository.findWithAttributesAndReviewsById(placeId)
                .orElseThrow(()-> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));

        long reviewCount = reviewRepository.countByPlace(place);

        Double averageRating = reviewRepository.getAverageRatingByPlace(place);
        if (averageRating == null) {
            averageRating = 0.0;
        }

        boolean isBookmarked = false;
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                isBookmarked = savedPlaceRepository.findByUserAndPlace(user, place).isPresent();
            }
        }

        return PlaceConverter.toPlaceDetail(place, (int) reviewCount, averageRating, isBookmarked);
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