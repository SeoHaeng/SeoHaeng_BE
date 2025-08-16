package com.seohaeng.backend.domain.bookChallenge.entity;

import com.seohaeng.backend.domain.common.entity.BaseEntity;
import com.seohaeng.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class BookChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bookStoreName;

    // 선물 받은 책
    private String receivedBookTitle;
    private String receivedBookAuthor;
    private String receivedBookImage;
    private LocalDate receivedBookPubDate;

    // 선물 할 책
    private String givenBookTitle;
    private String givenBookAuthor;
    private String givenBookImage;
    private LocalDate givenBookPubDate;

    @Builder.Default
    @Setter
    private boolean accepted = false; // 북챌린지 인증 글이 작성 되었는지에 대한 여뷰
}
