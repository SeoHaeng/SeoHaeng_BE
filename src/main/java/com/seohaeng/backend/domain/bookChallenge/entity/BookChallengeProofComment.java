package com.seohaeng.backend.domain.bookChallenge.entity;

import com.seohaeng.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BookChallengeProofComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String bookChallengeProofCommentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_challenge_proof_id", nullable = false)
    private BookChallengeProof bookChallengeProof;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
