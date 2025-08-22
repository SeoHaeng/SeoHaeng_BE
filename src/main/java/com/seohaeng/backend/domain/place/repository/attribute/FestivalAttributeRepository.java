package com.seohaeng.backend.domain.place.repository.attribute;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestivalAttributeRepository extends JpaRepository<FestivalAttribute, Long> {
    Optional<FestivalAttribute> findByPlace(Place place);
}
