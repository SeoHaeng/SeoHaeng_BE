package com.seohaeng.backend.domain.place.entity.placeAttribute;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.place.entity.place.Place;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAttribute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;


    private String firstmenu;
    private String treatmenu;

    private String kidsfacility;
    private String isSmokingAllowed;
    private String isTakeoutAvailable;
    private String hasParking;
    private String isReservable;
}
