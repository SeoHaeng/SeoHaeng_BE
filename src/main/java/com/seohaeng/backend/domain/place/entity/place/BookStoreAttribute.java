package com.seohaeng.backend.domain.place.entity.place;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class BookStoreAttribute extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    // 북카페, 북스테이
    private boolean bookCafe;
    private boolean bookStay;
    // 나중에 북챌린지 여부 이쪽으로 이동

    // 북살롱
    private boolean salonAll;
    private boolean readingClub;
    private boolean bookTalk;
    private boolean lecture;
    private boolean originalContent;
    private boolean bookWellage;

    // 편의 정보
    private boolean convenienceAll;
    private boolean spaceRental;
    private boolean parking;
    private boolean petFriendly;
    private boolean bookStorage;
    private boolean creatorSupport;
    private boolean bookOrder;
    private boolean bookDelivery;

    // 컬렉션북
    private boolean collectionAll;
    private boolean indiePublication;
    private boolean usedBooks;
    private boolean goods;
    private boolean artBook;
    private boolean illustrationBook;
    private boolean giftShop;
    private boolean souvenirs;

    // 테이스트
    private boolean tasteAll;
    private boolean pub;
    private boolean cafe;
    private boolean snack;
}