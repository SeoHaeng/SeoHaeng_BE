package com.seohaeng.backend.readingSpotComment.entity;

import com.seohaeng.backend.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReadingSpotComment {

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