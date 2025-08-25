package com.seohaeng.backend.domain.place.repository.attribute;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TouristSpotAttributeRepository extends JpaRepository<TouristSpotAttribute, Long> {
    Optional<TouristSpotAttribute> findByPlace(Place place);
}