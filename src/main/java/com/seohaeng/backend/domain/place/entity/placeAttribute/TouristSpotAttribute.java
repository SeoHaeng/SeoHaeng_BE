package com.seohaeng.backend.domain.place.entity.placeAttribute;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.place.entity.place.Place;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TouristSpotAttribute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Lob
    private String overview;

    private String parkingAvailable;
    private String petsAllowed;
    private String babyCarriageAllowed;
    private String creditCardAccepted;
}