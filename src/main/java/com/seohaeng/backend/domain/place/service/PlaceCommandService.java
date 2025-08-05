package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.SavedPlace;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.SavedPlaceRepository;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotLike;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceCommandService {

    private final SavedPlaceRepository savedPlaceRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public PlaceResponseDTO.PlaceBookmarkToggleResponse toggleBookMarkPlace(Long placeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId).
                orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));

        Optional<SavedPlace> userSaved
                = savedPlaceRepository.findByUserAndPlace(user,place);

        if (userSaved.isPresent()) {
            savedPlaceRepository.delete(userSaved.get());
            return new PlaceResponseDTO.PlaceBookmarkToggleResponse(false);
        } else {
            SavedPlace newSaved = SavedPlace.builder()
                    .user(user)
                    .place(place)
                    .build();
            savedPlaceRepository.save(newSaved);
            return new PlaceResponseDTO.PlaceBookmarkToggleResponse(true);
        }
    }
}
