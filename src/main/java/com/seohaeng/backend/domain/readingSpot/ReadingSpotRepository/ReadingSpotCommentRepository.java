package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingSpotCommentRepository extends JpaRepository<ReadingSpotComment, Long> {
}
