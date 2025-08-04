package com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingSpotCommentRepository extends JpaRepository<ReadingSpotComment, Long> {
    Page<ReadingSpotComment> findAllByReadingSpot(ReadingSpot readingSpot, Pageable pageable);
}
