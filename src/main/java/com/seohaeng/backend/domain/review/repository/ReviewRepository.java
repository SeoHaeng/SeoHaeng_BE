package com.seohaeng.backend.domain.review.repository;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByPlace(Place place, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.place = :place")
    Double getAverageRatingByPlace(@Param("place") Place place);
    
    long countByPlace(Place place);
}