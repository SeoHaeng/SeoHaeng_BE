package com.seohaeng.backend.domain.place.repository;

import com.seohaeng.backend.domain.place.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findAllByBookChallengeStatusTrue(Pageable pageable);
}