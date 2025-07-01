package com.seohaeng.backend.domain.travelCourse.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class TravelCourseSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer day;

    @Column(nullable = false)
    private Integer orderInDay;
}
