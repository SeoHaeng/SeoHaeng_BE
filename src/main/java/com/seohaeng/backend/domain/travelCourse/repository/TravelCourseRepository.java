package com.seohaeng.backend.domain.travelCourse.repository;

import com.seohaeng.backend.domain.travelCourse.entity.TravelCourse;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {

    @EntityGraph(attributePaths = {"travelCourseRegionList","user"})
    Optional<TravelCourse> findWithRegionById(Long id);

    @EntityGraph(attributePaths = {"travelCourseScheduleList"})
    Optional<TravelCourse> findWithCourseById(Long id);

    List<TravelCourse> findAllByUser(User user);
}
