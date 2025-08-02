package com.seohaeng.backend.domain.place.entity.place;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private String address;

    private String introduction;

    private String websiteUrl;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlaceImage> placeImages = new ArrayList<>();

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BookStoreAttribute bookStoreAttribute; // placeType이 BOOKSTORE일때만 사용

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean bookChallengeStatus = false;
}