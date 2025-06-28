package com.seohaeng.backend.domain.bookChallenge.entity;

import com.seohaeng.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BookGift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String bookTitle;

    @Column(nullable = false, length = 100)
    private String bookAuthor;

    @Column(nullable = false, length = 100)
    private String message;

    @Column(nullable = false, length = 2048)
    private String bookImageUrl;

    private Long giverId;

    private Long receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
