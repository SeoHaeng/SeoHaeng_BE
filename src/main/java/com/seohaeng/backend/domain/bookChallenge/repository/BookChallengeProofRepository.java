package com.seohaeng.backend.domain.bookChallenge.repository;

import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookChallengeProofRepository extends JpaRepository<BookChallengeProof, Long> {

    @Query("SELECT b FROM BookChallengeProof b LEFT JOIN FETCH b.bookChallengeProofImageList WHERE b.id = :id")
    Optional<BookChallengeProof> findWithImagesById(Long id);

    @Query("SELECT b FROM BookChallengeProof b LEFT JOIN FETCH b.bookChallengeProofComment WHERE b.id = :id")
    Optional<BookChallengeProof> findWithBookChallengeProofCommentById(Long id);

    @EntityGraph(attributePaths = "bookChallengeProofImageList")
    Page<BookChallengeProof> findAll(Pageable pageable);

    Optional<BookChallengeProof> findById(Long id);
}