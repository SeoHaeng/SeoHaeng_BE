package com.seohaeng.backend.domain.travelCourse.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regionName;

    private String regionImage;
}