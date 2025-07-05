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
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 회원 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4001", "존재하지 않는 사용자입니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "USER4002", "이미 사용 중인 username입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER4003", "비밀번호가 일치하지 않습니다."),
    PASSWORD_COMPLEXITY_FAIL(HttpStatus.BAD_REQUEST, "USER4004", "비밀번호는 영문자, 숫자, 특수문자를 모두 포함해야 합니다."),
    LOGIN_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4005", "사용자 로그인 정보가 존재하지 않습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "USER4006", "이미 사용 중인 nickname입니다."),

    // Auth 관련
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4001", "유효하지 않은 토큰입니다."),
    AUTH_EXTRACT_ERROR(HttpStatus.UNAUTHORIZED, "AUTH4002", "토큰 추출에 실패했습니다."),
    INVALID_REQUEST_INFO_KAKAO(HttpStatus.UNAUTHORIZED, "AUTH_007", "카카오 정보 불러오기에 실패하였습니다."),
    AUTH_INVALID_CODE(HttpStatus.UNAUTHORIZED, "", "코드가 유효하지 않습니다.");


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
