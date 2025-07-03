package com.seohaeng.backend.domain.user.controller;

import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.service.UserCommandService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value="/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ApiResponse<String> joinService (@Valid @RequestBody UserRequestDTO.joinDTO request){
        LoginInfo newUser = userCommandService.joinUser(request);
        return ApiResponse.onSuccess("회원가입 성공");
    }
}
