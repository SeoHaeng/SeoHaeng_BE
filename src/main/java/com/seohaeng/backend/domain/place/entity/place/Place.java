package com.seohaeng.backend.domain.place.entity.place;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;
import com.seohaeng.backend.domain.travelCourse.entity.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(nullable = false)
    private Long contentId;

    private String openingTime;

    private String address;

    private String websiteUrl;

    private Double latitude;

    private Double longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BookStoreAttribute bookStoreAttribute;
    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RestaurantAttribute restaurantAttribute;
    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private FestivalAttribute festivalAttribute;
    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TouristSpotAttribute touristSpotAttribute;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlaceImage> placeImages = new ArrayList<>();
}