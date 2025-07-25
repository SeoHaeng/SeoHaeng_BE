package com.seohaeng.backend.domain.travelCourse.repository;

import com.seohaeng.backend.domain.travelCourse.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByRegionName(String regionName);
}
