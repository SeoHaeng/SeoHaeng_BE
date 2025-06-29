package com.seohaeng.backend.domain.bookChallenge.entity;

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
public class BookChallengeProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String bookChallengeProofContent;

    @Column(nullable = false)
    private Integer bookChallengeProofLikes = 0;

    @OneToMany(mappedBy = "bookChallengeProof", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookChallengrProofImage> BookChallengrProofImageList = new ArrayList<>();

    @OneToMany(mappedBy = "bookChallengeProof", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookChallengeProofComment> bookChallengeProofComment = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
