package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingSpotImageRepository extends JpaRepository<ReadingSpotImage, Long> {
    Optional<ReadingSpotImage> findByReadingSpotAndIsMainTrue(ReadingSpot readingSpot);

}
