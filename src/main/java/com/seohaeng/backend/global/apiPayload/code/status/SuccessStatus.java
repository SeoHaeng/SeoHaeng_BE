package com.seohaeng.backend.global.apiPayload.code.status;

import com.seohaeng.backend.global.apiPayload.code.BaseCode;
import com.seohaeng.backend.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    BOOK_CHALLENGE_PROOF_LIKE_TOGGLED(HttpStatus.OK, "BOOK_CHALLENGE2001", "북챌린지 게시글 좋아요 토글이 완료되었습니다."),
    REVIEW_CREATE_SUCCESS(HttpStatus.OK, "REVIEW_2001", "리뷰 작성이 완료되었습니다."),
    READING_SPOT_CREATE_SUCCESS(HttpStatus.OK, "READING_SPOT_2001", "공간책갈피 생성이 완료되었습니다."),
    READING_SPOT_LIKE_TOGGLED(HttpStatus.OK, "READING_SPOT2002", "공간책갈피 좋아요 토글이 완료되었습니다."),
    READING_SPOT_SCRAP_TOGGLED(HttpStatus.OK, "READING_SPOT2003", "공간책갈피 스크랩 토글이 완료되었습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
