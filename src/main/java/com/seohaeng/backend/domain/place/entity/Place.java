package com.seohaeng.backend.domain.place.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private String address;

    private String introduction;

    private String websiteUrl;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean bookChallengeStatus = false;
}