package com.seohaeng.backend.domain.bookChallenge.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BookChallengeProofImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @Builder.Default
    private boolean isMain = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_challenge_proof_id", nullable = false)
    private BookChallengeProof bookChallengeProof;
}