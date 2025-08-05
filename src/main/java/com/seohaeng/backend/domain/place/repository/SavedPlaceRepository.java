package com.seohaeng.backend.domain.place.repository;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.SavedPlace;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedPlaceRepository extends JpaRepository<SavedPlace, Long> {
    Optional<SavedPlace> findByUserAndPlace(User user, Place place);
}
