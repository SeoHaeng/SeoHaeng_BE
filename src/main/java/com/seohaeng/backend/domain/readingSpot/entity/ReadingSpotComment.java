package com.seohaeng.backend.domain.readingSpot.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReadingSpotComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_spot_id", nullable = false)
    private ReadingSpot readingSpot;

    @Column(nullable = false)
    private String content;

}