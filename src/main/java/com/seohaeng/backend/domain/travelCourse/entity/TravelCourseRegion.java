package com.seohaeng.backend.domain.travelCourse.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class TravelCourseRegion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelCourse_id", nullable = false)
    private TravelCourse travelCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reigon_id", nullable = false)
    private Region region;
}