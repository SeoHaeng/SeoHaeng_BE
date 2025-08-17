package com.seohaeng.backend.domain.user.controller;

import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.service.UserCommandService;
import com.seohaeng.backend.domain.user.service.UserQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Operation(summary = "일반 회원가입 API",
            description = "아이디는 4~12자이며, 비밀번호는 8~20자이고 영문, 숫자, 특수문자를 반드시 포함해야 합니다." +
                    "비밀번호 확인란(password2)은 password1과 반드시 일치해야 합니다.")
    @PostMapping("/auth/signup")
    public ApiResponse<String> joinService (@Valid @RequestBody UserRequestDTO.joinDTO request){
        userCommandService.joinUser(request);
        return ApiResponse.onSuccess("회원가입이 완료되었습니다.");
    }

    @Operation(summary = "일반 로그인 API",
            description = "아이디와 비밀번호를 정확히 입력하면 인증에 성공하며, Access Token이 발급됩니다.")
    @PostMapping("/auth/login")
    public ApiResponse<UserResponseDTO.LoginResultDTO> localLogin (@RequestBody UserRequestDTO.LoginDTO request){
        return ApiResponse.onSuccess(userCommandService.loginUser(request));
    }

    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 및 회원 가입을 진행하는 API입니다. 인가코드를 넘겨주세요.")
    @GetMapping("/auth/kakao")
    public ApiResponse<UserResponseDTO.LoginResultDTO> kakaoLogin(@RequestParam("code") String code) {
        return ApiResponse.onSuccess(userCommandService.kakaoLogin(code));
    }

    @Operation(summary = "닉네임 중복확인 API", description = "닉네임 중복확인을 진행하는 API입니다.")
    @GetMapping("/auth/check-nickname")
    public ApiResponse<String> checkNickname(
            @AuthUser Long userId,
            @RequestParam String nickname) {
        String result = userQueryService.checkNickname(userId, nickname);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "아이디 중복확인 API", description = "아이디 중복확인을 진행하는 API입니다.")
    @GetMapping("/auth/check-username")
    public ApiResponse<String> checkUsername(
            @AuthUser Long userId,
            @RequestParam String username) {
        String result = userQueryService.checkUsername(userId, username);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "사용자 ID 주입 테스트", description = "@AuthUser을 사용한 현사용자 ID 자동 주입 예시입니다.")
    @GetMapping("test")
    public ApiResponse<String> loginTest(@AuthUser Long userId){
        String result = "userID : " + userId;
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "ID로 회원정보 조회", description = "정보(닉네임, 프로필사진 등)을 조회하고자 하는 사용자의 ID를 넘겨주세요")
    @GetMapping("/{userId}")
    public ApiResponse<UserResponseDTO.GetUserInfoResponseDTO> getUserInfo(@PathVariable("userId") Long userId){
        UserResponseDTO.GetUserInfoResponseDTO result = userQueryService.getUserInfo(userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = " 내 정보 조회", description = "현재 사용자의 프로필 정보를 조회합니다.")
    @GetMapping
    public ApiResponse<UserResponseDTO.GetMyInfoResponseDTO> getMyInfo(@AuthUser Long userId){
        UserResponseDTO.GetMyInfoResponseDTO result = userQueryService.getMyInfo(userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "내 정보 수정", description = "현재 사용자의 정보를 수정합니다.")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponseDTO.GetUserInfoResponseDTO> updateMyInfo(
            @AuthUser Long userId,
            @Valid @RequestPart("request") UserRequestDTO.updateProfileDTO request,
            @RequestPart(value = "profileImage", required = false) 
            @Parameter(description = "업로드할 프로필 이미지 파일") MultipartFile image,
            @RequestParam(value = "useDefault", required = false, defaultValue = "false") 
            @Parameter(description = "기본 프로필 사진 사용 여부 (true: 기본 프로필로 변경, false: 업로드된 이미지 사용)") Boolean useDefault
    ) {
        UserResponseDTO.GetUserInfoResponseDTO result = userCommandService.updateUserInfo(userId, request, useDefault, image);
        return ApiResponse.onSuccess(result);
    }
}