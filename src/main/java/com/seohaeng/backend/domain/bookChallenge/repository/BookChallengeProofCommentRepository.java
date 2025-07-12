package com.seohaeng.backend.domain.bookChallenge.repository;

import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookChallengeProofCommentRepository extends JpaRepository<BookChallengeProofComment, Long> {
    Page<BookChallengeProofComment> findAllByBookChallengeProof (Pageable pageable, BookChallengeProof bookChallengeProof);
}
