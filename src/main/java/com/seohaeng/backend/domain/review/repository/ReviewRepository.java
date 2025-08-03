package com.seohaeng.backend.domain.review.repository;

import com.seohaeng.backend.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
