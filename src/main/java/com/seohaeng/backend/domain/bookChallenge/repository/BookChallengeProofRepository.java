package com.seohaeng.backend.domain.bookChallenge.repository;

import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookChallengeProofRepository extends JpaRepository<BookChallengeProof, Long> {

    @Query("SELECT bcp FROM BookChallengeProof bcp JOIN FETCH bcp.bookChallengeProofImageList WHERE bcp.id = :id")
    Optional<BookChallengeProof> findWithImagesById(@Param("id") Long bookChallengeProofId);
}