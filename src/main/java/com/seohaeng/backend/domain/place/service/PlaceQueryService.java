package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.converter.PlaceConverter;
import com.seohaeng.backend.domain.place.dto.PlaceInfoDTO;
import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.BookChallengeEvent;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.SavedPlace;
import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.repository.BookChallengeEventRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.BookStoreAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import com.seohaeng.backend.domain.place.repository.SavedPlaceRepository;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.review.repository.ReviewRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.seohaeng.backend.domain.place.converter.PlaceConverter.toBookChallengeEventDto;

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
    private final BookChallengeEventRepository bookChallengeEventRepository;

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

    // 북챌린지 서점 이벤트 조회
    public PlaceResponseDTO.BookChallengeEventDto findBookChallengeEvents(Long placeId){

        Place place = placeRepository.findWithAttributesById(placeId)
                .orElseThrow(()-> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));
        BookStoreAttribute bookStoreAttribute = place.getBookStoreAttribute();
        if (bookStoreAttribute == null) {
            throw new PlaceHandler(ErrorStatus.NOT_INDEPENDENT_BOOKSTORE);
        }
        if (!bookStoreAttribute.isBookChallengeStatus()) {
            throw new PlaceHandler(ErrorStatus.BOOK_CHALLENGE_NOT_IN_PROGRESS);
        }
        BookChallengeEvent bookChallengeEvent = bookChallengeEventRepository.findByPlace(place);
        if (bookChallengeEvent == null) {
            throw new PlaceHandler(ErrorStatus.BOOK_CHALLENGE_NOT_IN_PROGRESS);
        }
        return toBookChallengeEventDto(bookChallengeEvent, bookChallengeEvent.getBookChallengeEventImages());
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

    // 찜한 장소 조회
    public List<PlaceResponseDTO.SavedPlaceInfoDTO> getBookMarkPlace(
            Long userId, Double currentLat, Double currentLng) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<SavedPlace> savedPlaces = savedPlaceRepository.findByUserOrderByCreatedAtDesc(user);

        return savedPlaces.stream()
                .map(savedPlace -> {
                    Place place = savedPlace.getPlace();
                    
                    List<Review> reviews = place.getReviews();
                    int reviewCount = reviews.size();
                    double averageRating = 0.0;
                    if (reviewCount > 0) {
                        double sum = reviews.stream()
                                .mapToDouble(r -> r.getRating().doubleValue())
                                .sum();
                        averageRating = BigDecimal.valueOf(sum / reviewCount)
                                .setScale(1, RoundingMode.HALF_UP)
                                .doubleValue();
                    }

                    double distance = calculateDistance(currentLat, currentLng, place.getLatitude(), place.getLongitude());

                    return PlaceConverter.toSavedPlaceInfoDTO(
                            place, true, averageRating, reviewCount, distance);
                })
                .collect(Collectors.toList());
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if(lat1 == null || lon1 == null || lat2 == null || lon2 == null) return 0.0;
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(R * c * 10) / 10.0;
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