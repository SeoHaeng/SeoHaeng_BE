package com.seohaeng.backend.domain.user.repository;

import com.seohaeng.backend.domain.user.entity.Agreement;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    Optional<Agreement> findByUser (User user);
}
