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
    CURRENT_USERNAME(HttpStatus.BAD_REQUEST, "USER4007", "현재 사용 중인 아이디입니다."),
    CURRENT_NICKNAME(HttpStatus.BAD_REQUEST, "USER4008", "현재 사용 중인 닉네임입니다."),
    OWNER_NOT_FOUND(HttpStatus.FORBIDDEN, "OWNER_001", "사장님 계정이 아닙니다. 권한이 없습니다."),
    AGREEMENT_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "AGREEMENT_001", "이미 모든 약관에 동의를 했습니다."),
    AGREEMENT_NOT_FOUND(HttpStatus.FORBIDDEN, "AGREEMENT_002", "현재 사용자의 이용 약관 동의 여부를 찾을 수 없습니다."),
    AGREEMENT_NOT_COMPLETED(HttpStatus.FORBIDDEN, "AGREEMENT_003", "모든 약관에 동의하지 않았습니다."),

    // Auth 관련
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4001", "유효하지 않은 토큰입니다."),
    AUTH_EXTRACT_ERROR(HttpStatus.UNAUTHORIZED, "AUTH4002", "토큰 추출에 실패했습니다."),
    INVALID_REQUEST_INFO_KAKAO(HttpStatus.UNAUTHORIZED, "AUTH_007", "카카오 정보 불러오기에 실패하였습니다."),
    INVALID_REQUEST_INFO_NAVER(HttpStatus.UNAUTHORIZED, "AUTH_008", "네이버 정보 불러오기에 실패하였습니다."),
    INVALID_REQUEST_INFO_GOOGLE(HttpStatus.UNAUTHORIZED, "AUTH_009", "구글 정보 불러오기에 실패하였습니다."),
    AUTH_INVALID_CODE(HttpStatus.UNAUTHORIZED, "", "코드가 유효하지 않습니다."),

    // 장소 관련
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE_4001", "존재하지 않는 시설입니다."),

    // 지역 관련
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION_4001", "존재하지 않는 지역입니다. 지역 ID를 다시 확인해주세요"),
    REGION_NOT_GANGWON(HttpStatus.BAD_REQUEST, "REGION_4002", "강원도에 해당하는 지역이 아닙니다. 주소를 다시 확인해주세요"),

    // 여행 일정 관련
    TRAVEL_COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRAVEL_COURSE_4001", "존재하지 않는 여행 일정입니다. 여행 일정 ID를 다시 확인해주세요"),

    // 북챌린지 관련
    BOOK_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK_CHALLENGE_4001", "해당하는 북챌린지가 없습니다."),
    BOOK_CHALLENGE_PROOF_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK_CHALLENGE_4001", "해당하는 북챌린지 인증글이 없습니다."),
    BOOK_CHALLENGE_NOT_EXIST(HttpStatus.NOT_FOUND, "BOOK_CHALLENGE_4002", "해당 사용자는 현재 진행중인 북챌린지가 없습니다."),
    BOOK_CHALLENGE_ALREADY_DONE(HttpStatus.NOT_FOUND, "BOOK_CHALLENGE_4003", "이미 인증글이 작성된 북챌린지 입니다.(이미 끝난 북챌린지)"),
    BOOK_CHALLENGE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "BOOK_CHALLENGE_4004", "이미 아직 인증글 작성이 되지않은 북챌린지가 있습니다. 새로운 북챌린지 생성은 불가능합니다."),

    // 리뷰 관련
    REVIEW_INVALID_ISSUE(HttpStatus.BAD_REQUEST, "REVIEW_4001", "리뷰 별점은 0.5점 단위로 입력해야합니다."),

    // 공간 책갈피 관련
    READING_SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "READING_SPOT_4001", "공간 책갈피가 존재하지 않습니다."),
    ;

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
