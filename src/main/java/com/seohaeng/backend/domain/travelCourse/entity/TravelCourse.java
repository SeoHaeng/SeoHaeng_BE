package com.seohaeng.backend.domain.travelCourse.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class TravelCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String travelCourseTitle;

    @Column(nullable = false, length = 20)
    private LocalDate travelCourseStartDate;

    @Column(nullable = false, length = 20)
    private LocalDate travelCourseEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "travelCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelCourseRegion> travelCourseRegionList;

    @OneToMany(mappedBy = "travelCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelCourseSchedule> travelCourseScheduleList;
}
