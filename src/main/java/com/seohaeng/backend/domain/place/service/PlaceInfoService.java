package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceInfoDTO;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.SavedPlaceRepository;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceInfoService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final SavedPlaceRepository savedPlaceRepository;

    public PlaceInfoDTO getPlaceInfo(Long placeId, Long userId, Double currentLat, Double currentLng) {
        Place place = placeRepository.findWithAttributesById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장소입니다."));

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

        boolean bookmarked = false;
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            bookmarked = savedPlaceRepository.findByUserAndPlace(user, place).isPresent();
        }

        double distance = calculateDistance(currentLat, currentLng, place.getLatitude(), place.getLongitude());

        return new PlaceInfoDTO(
                place.getId(),
                place.getName(),
                place.getPlaceType().name(),
                bookmarked,
                averageRating,
                reviewCount,
                distance,
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getPlaceImages() != null && !place.getPlaceImages().isEmpty() 
                    ? place.getPlaceImages().get(0).getImageUrl() : "https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/places/default.png"
        );
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

}
