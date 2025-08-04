package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingSpotImageRepository extends JpaRepository<ReadingSpotImage, Long> {
}
