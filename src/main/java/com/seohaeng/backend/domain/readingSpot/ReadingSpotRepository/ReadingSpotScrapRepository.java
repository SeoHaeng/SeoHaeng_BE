package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotScrap;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingSpotScrapRepository extends JpaRepository<ReadingSpotScrap, Long> {
    Optional<ReadingSpotScrap> findByUserAndReadingSpot(User user, ReadingSpot readingSpot);
}
