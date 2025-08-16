package com.seohaeng.backend.domain.user.service;

import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.AuthException;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.seohaeng.backend.domain.user.converter.UserConverter.toUserInfoDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final LoginInfoRepository loginInfoRepository;

    public UserResponseDTO.GetUserInfoResponseDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        return toUserInfoDTO(user);
    }

    public String checkUsername(String username) {
        boolean exists = loginInfoRepository.existsByUsername(username);
        if (!exists) {
            return "사용 가능한 아이디입니다.";
        } else {
            return "이미 사용 중인 아이디입니다.";
        }
    }

    public String checkNickname(String nickname) {
        boolean exists = userRepository.existsByNickname(nickname);
        if (!exists) {
            return "사용 가능한 닉네임입니다.";
        } else {
            return "이미 사용 중인 닉네임입니다.";
        }
    }
}