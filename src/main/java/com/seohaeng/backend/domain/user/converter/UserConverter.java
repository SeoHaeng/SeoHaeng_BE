package com.seohaeng.backend.domain.user.converter;

import com.seohaeng.backend.domain.user.dto.KakaoProfile;
import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.Provider;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;

public class UserConverter {

    public static User toUser(UserRequestDTO.joinDTO request){
        User user = User.builder()
                .nickname(request.getNickname())
                .imageUrl("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png")
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
                .imageUrl("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png")
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

    public static UserResponseDTO.LoginResultDTO toLoginResultDTO(
            Long userId,String accessToken, String refreshToken){
        return UserResponseDTO.LoginResultDTO.builder()
                .UserId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static UserResponseDTO.GetUserInfoResponseDTO toUserInfoDTO(User user) {
        return UserResponseDTO.GetUserInfoResponseDTO.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .profileImageUrl(user.getImageUrl())
                .build();
    }

    public static UserResponseDTO.GetMyInfoResponseDTO toMyInfoDTO(User user, LoginInfo loginInfo) {
        return UserResponseDTO.GetMyInfoResponseDTO.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .profileImageUrl(user.getImageUrl())
                .loginType(loginInfo.getProvider())
                .build();
    }
}
