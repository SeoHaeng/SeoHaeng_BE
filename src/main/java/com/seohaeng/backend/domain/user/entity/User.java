package com.seohaeng.backend.domain.user.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private String imageUrl;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LoginInfo loginInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Stamp stamp;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TravelCourse> travelCourses = new ArrayList<>();
}