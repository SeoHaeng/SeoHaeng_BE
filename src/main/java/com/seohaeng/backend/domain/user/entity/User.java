package com.seohaeng.backend.domain.user.entity;

import com.seohaeng.backend.domain.bookChallenge.entity.BookChallenge;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofComment;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofLike;
import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.notification.entity.Notification;
import com.seohaeng.backend.domain.place.entity.place.SavedPlace;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotLike;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotScrap;
import com.seohaeng.backend.domain.review.entity.Review;
import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    private String imageUrl;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LoginInfo loginInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Agreement agreement;

    // 독립 서점 사장 여부
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Owner owner;

    // 북 챌린지
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookChallenge> bookChallenges = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookChallengeProof> bookChallengeProofList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookChallengeProofComment> bookChallengeProofCommentList = new ArrayList<>();

    // 공간 책갈피
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpot> readingSpotList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpotLike> readingSpotLikeList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpotScrap> readingSpotScrapList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpotComment> readingSpotCommentList = new ArrayList<>();

    // 여행 일정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Stamp> stampList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TravelCourse> travelCourses = new ArrayList<>();

    // 장소 저장
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SavedPlace> savedPlaceList = new ArrayList<>();

    // 리뷰
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();

    // 알림
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notification> notificationList = new ArrayList<>();

    // 북 챌린지 인증 좋아요
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookChallengeProofLike> bookChallengeProofLikeList = new ArrayList<>();
}