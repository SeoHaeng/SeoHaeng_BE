package com.seohaeng.backend.domain.place.repository;

import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.place.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @EntityGraph(attributePaths = {"bookStoreAttribute", "restaurantAttribute", "festivalAttribute", "touristSpotAttribute", "placeImages"})
    Optional<Place> findWithAttributesById(Long id);

    @EntityGraph(attributePaths = {"bookStoreAttribute", "restaurantAttribute", "festivalAttribute", "touristSpotAttribute", "placeImages"})
    Optional<Place> findWithAttributesAndReviewsById(Long id);

    Optional<Place> findByContentId(String contentId);

    @Query(value = "SELECT * FROM place WHERE place_type = :placeType ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Place> findRandomByPlaceType(@Param("placeType") String placeType);

    List<Place> findByNameContaining(String keyword);

    List<Place> findByPlaceType(PlaceType placeType);

    @Query("SELECT p FROM Place p JOIN p.festivalAttribute f WHERE f.startDate <= :currentDate AND f.endDate >= :currentDate")
    List<Place> findOngoingFestivals(@Param("currentDate") LocalDate currentDate);

}
