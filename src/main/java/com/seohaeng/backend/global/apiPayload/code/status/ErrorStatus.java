package com.seohaeng.backend.global.apiPayload.code.status;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),

    // 회원 관련
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "USER4001", "이미 사용 중인 username입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER4002", "비밀번호가 일치하지 않습니다."),
    PASSWORD_COMPLEXITY_FAIL(HttpStatus.BAD_REQUEST, "USER4003", "비밀번호는 영문자, 숫자, 특수문자를 모두 포함해야 합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
