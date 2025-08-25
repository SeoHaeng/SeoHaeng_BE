package com.seohaeng.backend.domain.place.entity.place;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.place.entity.enums.PlaceType;
import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.travelCourse.entity.Region;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(nullable = false, updatable = true)
    private String contentId;

    @Column(length = 1000)
    private String useTime;

    private String address;

    @Column(length = 1000)
    private String websiteUrl;

    private String tel;

    private Double latitude;

    private Double longitude;

    private String modifiedtime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookStoreAttribute bookStoreAttribute;
    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RestaurantAttribute restaurantAttribute;
    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FestivalAttribute festivalAttribute;
    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TouristSpotAttribute touristSpotAttribute;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlaceImage> placeImages = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

}