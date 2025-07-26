package com.seohaeng.backend.domain.travelCourse.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Stamp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    private LocalDate stampedDate;

    public void setStampedDate(LocalDate stampedDate) {
        this.stampedDate = stampedDate;
    }
}