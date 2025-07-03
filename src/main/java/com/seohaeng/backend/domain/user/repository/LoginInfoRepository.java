package com.seohaeng.backend.domain.user.repository;

import com.seohaeng.backend.domain.user.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

    @Query("SELECT l FROM LoginInfo l JOIN FETCH l.user WHERE l.email = :email")
    Optional<LoginInfo> findByEmailWithUser(@Param("email") String email);

    @Query("SELECT l FROM LoginInfo l JOIN FETCH l.user WHERE l.username = :username")
    Optional<LoginInfo> findByUsernameWithUser(@Param("username") String username);

    Optional<LoginInfo> findByUsername(String username);

    Boolean existsByUsername(String username);
}