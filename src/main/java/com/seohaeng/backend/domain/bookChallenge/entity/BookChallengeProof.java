package com.seohaeng.backend.domain.bookChallenge.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BookChallengeProof extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 2048)
    private String presentMessage;

    @Column(nullable = false, length = 2048)
    private String bookChallengeProofContent;

    @Builder.Default
    @Column(nullable = false)
    private Integer bookChallengeProofLikes = 0;

    @Column(nullable = false, length = 30)
    private String receivedBookTitle;

    @Column(nullable = false, length = 30)
    private String receivedBookAuthor;

    @Column(nullable = false, length = 300)
    private String receivedBookImage;

    @Column(nullable = false, length = 30)
    private String givenBookTitle;

    @Column(nullable = false, length = 30)
    private String givenBookAuthor;

    @Column(nullable = false, length = 300)
    private String givenBookImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Builder.Default
    @OneToMany(mappedBy = "bookChallengeProof", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookChallengeProofImage> bookChallengeProofImageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "bookChallengeProof", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookChallengeProofComment> bookChallengeProofComment = new ArrayList<>();
}