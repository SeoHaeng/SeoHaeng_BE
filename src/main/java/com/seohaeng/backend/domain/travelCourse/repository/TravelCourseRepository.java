package com.seohaeng.backend.domain.travelCourse.repository;

import com.seohaeng.backend.domain.travelCourse.entity.TravelCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Integer> {
}
