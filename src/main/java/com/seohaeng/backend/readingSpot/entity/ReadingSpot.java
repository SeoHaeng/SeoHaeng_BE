package com.seohaeng.backend.readingSpot.entity;

import com.seohaeng.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReadingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String imageUrl;

    private String content;

    private int likes;

    private String bookTitle;

    private String bookAuthor;

    private LocalDateTime bookPubDate;

    private String bookImageUrl;

}