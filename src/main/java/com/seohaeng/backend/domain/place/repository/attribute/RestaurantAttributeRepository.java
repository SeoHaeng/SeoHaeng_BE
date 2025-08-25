package com.seohaeng.backend.domain.place.repository.attribute;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantAttributeRepository extends JpaRepository<RestaurantAttribute, Long> {

    Optional<RestaurantAttribute> findByPlace(Place place);
}
