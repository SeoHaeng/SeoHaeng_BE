package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingSpotRepository extends JpaRepository<ReadingSpot, Long> {
}
