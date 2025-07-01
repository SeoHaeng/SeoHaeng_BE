package com.seohaeng.backend.domain.bookChallenge.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BookChallengrProofImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_challenge_proof_id", nullable = false)
    private BookChallengeProof bookChallengeProof;
}
