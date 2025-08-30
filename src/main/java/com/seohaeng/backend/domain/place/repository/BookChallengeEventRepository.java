package com.seohaeng.backend.domain.place.repository;

import com.seohaeng.backend.domain.place.entity.place.BookChallengeEvent;
import com.seohaeng.backend.domain.place.entity.place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookChallengeEventRepository extends JpaRepository<BookChallengeEvent, Long> {
    @EntityGraph(attributePaths = "place")
    Page<BookChallengeEvent> findAll (Pageable pageable);
    
    @EntityGraph(attributePaths = "bookChallengeEventImages")
    BookChallengeEvent findByPlace(Place place);
}