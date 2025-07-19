package com.seohaeng.backend.domain.travelCourse.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Stamp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean chuncheon;

    @Column(nullable = false)
    private Boolean wonju;

    @Column(nullable = false)
    private Boolean gangneung;

    @Column(nullable = false)
    private Boolean donghae;

    @Column(nullable = false)
    private Boolean taebaek;

    @Column(nullable = false)
    private Boolean sokcho;

    @Column(nullable = false)
    private Boolean samcheok;

    @Column(nullable = false)
    private Boolean hongcheon;

    @Column(nullable = false)
    private Boolean hoengseong;

    @Column(nullable = false)
    private Boolean yeongwol;

    @Column(nullable = false)
    private Boolean pyeongchang;

    @Column(nullable = false)
    private Boolean jeongseon;

    @Column(nullable = false)
    private Boolean cheorwon;

    @Column(nullable = false)
    private Boolean hwacheon;

    @Column(nullable = false)
    private Boolean yanggu;

    @Column(nullable = false)
    private Boolean inje;

    @Column(nullable = false)
    private Boolean goseong;

    @Column(nullable = false)
    private Boolean yangyang;
}
