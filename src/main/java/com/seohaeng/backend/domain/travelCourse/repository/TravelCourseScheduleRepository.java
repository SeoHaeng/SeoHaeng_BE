package com.seohaeng.backend.domain.travelCourse.repository;

import com.seohaeng.backend.domain.travelCourse.entity.TravelCourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelCourseScheduleRepository extends JpaRepository<TravelCourseSchedule, Integer> {
}
