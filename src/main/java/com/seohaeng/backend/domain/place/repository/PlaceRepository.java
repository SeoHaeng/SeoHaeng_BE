package com.seohaeng.backend.domain.place.repository;

import com.seohaeng.backend.domain.place.entity.place.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @EntityGraph(attributePaths = {"bookStoreAttribute", "restaurantAttribute", "festivalAttribute", "touristSpotAttribute", "placeImages"})
    Optional<Place> findWithAttributesById(Long id);

    Optional<Place> findByContentId(String contentId);

    @Query(value = "SELECT * FROM place WHERE place_type = :placeType ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Place> findRandomByPlaceType(@Param("placeType") String placeType);
}