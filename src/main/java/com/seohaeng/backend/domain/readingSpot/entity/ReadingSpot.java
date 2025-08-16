package com.seohaeng.backend.domain.readingSpot.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.travelCourse.entity.Region;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ReadingSpot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    private String address;
    private Double latitude;
    private Double longitude;

    private int templateId;
    private String title;
    private String content;

    @Builder.Default
    private int likes = 0;

    @Builder.Default
    private int scraps = 0;

    private String bookTitle;
    private String bookAuthor;
    private LocalDate bookPubDate;
    private String bookImageUrl;

    private boolean opened;

    @OneToMany(mappedBy = "readingSpot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpotImage> readingSpotImageList = new ArrayList<>();

    @OneToMany(mappedBy = "readingSpot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpotComment> readingSpotCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "readingSpot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadingSpotLike> readingSpotLikeList = new ArrayList<>();

    public void increaseReadingSpotLikes(){
        this.likes++;
    }

    public void decreaseReadingSpotLikes(){
        this.likes--;
    }

    public void increaseReadingSpotScraps(){
        this.scraps++;
    }

    public void decreaseReadingSpotScraps(){
        this.scraps--;
    }
}