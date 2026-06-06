package com.seohaeng.backend.domain.readingSpot.repository;

import com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReadingSpotRepository extends JpaRepository<ReadingSpot, Long> {

    @EntityGraph(attributePaths = {"readingSpotImageList"})
    Optional<ReadingSpot> findWithReadingSpotImagesById(Long id);

    Page<ReadingSpot> findAllByUser(User user, Pageable pageable);

    Page<ReadingSpot> findAllByOpenedTrue(Pageable pageable);

    @Query("SELECT new com.seohaeng.backend.domain.place.dto.PlaceMarkerDTO(r.id, r.title, r.latitude, r.longitude) " +
           "FROM ReadingSpot r " +
           "WHERE r.opened = true " +
           "AND r.latitude IS NOT NULL AND r.longitude IS NOT NULL " +
           "AND r.latitude BETWEEN :minLat AND :maxLat " +
           "AND r.longitude BETWEEN :minLng AND :maxLng")
    List<PlaceMarkerDTO> findMarkerDTOsByBounds(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );
}