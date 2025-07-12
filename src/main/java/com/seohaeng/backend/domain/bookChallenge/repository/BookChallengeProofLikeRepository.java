package com.seohaeng.backend.domain.bookChallenge.repository;

import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofLike;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookChallengeProofLikeRepository extends JpaRepository<BookChallengeProofLike, Long> {
    Optional<BookChallengeProofLike> findByUserAndBookChallengeProof(User user, BookChallengeProof bookChallengeProof);
}