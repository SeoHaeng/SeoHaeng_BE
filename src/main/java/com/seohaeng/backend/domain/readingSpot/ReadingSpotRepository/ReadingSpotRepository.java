package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingSpotRepository extends JpaRepository<ReadingSpot, Long> {

    @EntityGraph(attributePaths = {"readingSpotImageList"})
    Optional<ReadingSpot> findWithReadingSpotImagesById(Long id);
}
