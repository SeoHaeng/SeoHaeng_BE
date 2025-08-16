package com.seohaeng.backend.domain.user.repository;

import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    Optional<User>findById(Long id);

    @EntityGraph(attributePaths = {"stampList"})
    Optional<User> findWithStampListById(Long userId);

    Optional<User> findByNickname(String nickname);

    @EntityGraph(attributePaths = "loginInfo")
    Optional<User> findUserWithLoginInfoById(Long id);
}
