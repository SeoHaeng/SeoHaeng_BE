package com.seohaeng.backend.domain.user.service;

import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.seohaeng.backend.domain.user.converter.UserConverter.toMyInfoDTO;
import static com.seohaeng.backend.domain.user.converter.UserConverter.toUserInfoDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserResponseDTO.GetMyInfoResponseDTO getMyInfo(Long userId){
        User user = userRepository.findUserWithLoginInfoById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        LoginInfo loginInfo = user.getLoginInfo();
        return toMyInfoDTO(user, loginInfo);
    }

    public UserResponseDTO.GetUserInfoResponseDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        return toUserInfoDTO(user);
    }

    public String checkUsername(HttpServletRequest request, String username) {
        String token = jwtTokenProvider.resolveToken(request);
        boolean exists = loginInfoRepository.existsByUsername(username);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            if (!exists) {
                return "사용 가능한 아이디입니다.";
            } else {
                return "이미 사용 중인 아이디입니다.";
            }
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        User user = userRepository.findUserWithLoginInfoById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        String currentUsername = user.getLoginInfo().getUsername();
        
        if (Objects.equals(currentUsername, username)) {
            return "현재 사용중이신 아이디입니다.";
        } else if (!exists) {
            return "사용 가능한 아이디입니다.";
        } else {
            return "이미 사용 중인 아이디입니다.";
        }
    }

    public String checkNickname(HttpServletRequest request, String nickname) {
        String token = jwtTokenProvider.resolveToken(request);
        boolean exists = userRepository.existsByNickname(nickname);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            if (!exists) {
                return "사용 가능한 닉네임입니다.";
            } else {
                return "이미 사용 중인 닉네임입니다.";
            }
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        String currentNickname = user.getNickname();
        
        if (Objects.equals(currentNickname, nickname)) {
            return "현재 사용 중이신 닉네임입니다.";
        } else if (!exists) {
            return "사용 가능한 닉네임입니다.";
        } else {
            return "이미 사용 중인 닉네임입니다.";
        }
    }
}