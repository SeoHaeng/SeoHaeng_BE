package com.seohaeng.backend.domain.place.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private String introduction;

    private String websiteUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean bookChallengeStatus = false;
}