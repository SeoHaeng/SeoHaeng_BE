package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotLike;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingSpotLikeRepository extends JpaRepository<ReadingSpotLike, Long> {
    Optional<ReadingSpotLike> findByUserAndReadingSpot(User user, ReadingSpot readingSpot);
}
