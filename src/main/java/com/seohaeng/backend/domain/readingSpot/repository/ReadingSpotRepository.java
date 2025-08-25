package com.seohaeng.backend.domain.readingSpot.repository;

import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingSpotRepository extends JpaRepository<ReadingSpot, Long> {

    @EntityGraph(attributePaths = {"readingSpotImageList"})
    Optional<ReadingSpot> findWithReadingSpotImagesById(Long id);

    Page<ReadingSpot> findAllByUser(User user, Pageable pageable);
    
    Page<ReadingSpot> findAllByOpenedTrue(Pageable pageable);
}
