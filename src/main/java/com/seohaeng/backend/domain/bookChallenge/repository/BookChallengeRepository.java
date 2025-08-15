package com.seohaeng.backend.domain.bookChallenge.repository;

import com.seohaeng.backend.domain.bookChallenge.entity.BookChallenge;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookChallengeRepository extends JpaRepository<BookChallenge, Long> {
    Optional<BookChallenge> findFirstByUserOrderByCreatedAtDesc(User user);
}
