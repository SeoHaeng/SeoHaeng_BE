package com.seohaeng.backend.domain.user.entity;

import com.seohaeng.backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LoginInfo extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String provider;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}