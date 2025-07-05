package com.seohaeng.backend.domain.user.converter;

import com.seohaeng.backend.domain.user.dto.KakaoProfile;
import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.Provider;
import com.seohaeng.backend.domain.user.entity.User;

public class UserConverter {

    public static User toUser(UserRequestDTO.joinDTO request){
        User user = User.builder()
                .nickname(request.getNickname())
                .build();
        return user;
    }

    public static LoginInfo toLocalLoginInfo(UserRequestDTO.joinDTO request, User user){
        return LoginInfo.builder()
                .username(request.getUsername())
                .password(request.getPassword1())
                .provider(Provider.LOCAL)
                .user(user)
                .build();
    }

    public static User kakaoToUser(KakaoProfile kakaoProfile){
        String email = kakaoProfile.getKakaoAccount().getEmail();
        String nickname = email.substring(0, email.indexOf('@'));

        return User.builder()
                .nickname("kakao#" + nickname)
                .build();
    }

    public static LoginInfo toKakaoLoginInfo(KakaoProfile kakaoProfile, User user){
        return LoginInfo.builder()
                .username(kakaoProfile.getKakaoAccount().getEmail())
                .password("SOCIAL_LOGIN")
                .provider(Provider.KAKAO)
                .user(user)
                .build();
    }

    public static UserResponseDTO.LoginResultDTO toLoginResultDTO(Long userId,String accessToken){
        return UserResponseDTO.LoginResultDTO.builder()
                .UserId(userId)
                .accessToken(accessToken)
                .build();
    }

}
