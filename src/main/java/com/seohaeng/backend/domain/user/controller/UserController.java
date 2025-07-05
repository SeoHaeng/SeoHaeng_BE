package com.seohaeng.backend.domain.user.controller;

import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.service.UserCommandService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

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

    @Operation(summary = "사용자 ID 주입 테스트",
            description = "@AuthUser을 사용한 현사용자 ID 자동 주입 예시입니다.")
    @GetMapping("test")
    public ApiResponse<String> loginTest(@AuthUser Long userId){
        String result = "userID : " + userId;
        return ApiResponse.onSuccess(result);
    }
}
