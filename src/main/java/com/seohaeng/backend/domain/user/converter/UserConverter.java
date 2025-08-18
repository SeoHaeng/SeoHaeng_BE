package com.seohaeng.backend.domain.user.converter;

import com.seohaeng.backend.domain.user.dto.*;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.Provider;
import com.seohaeng.backend.domain.user.entity.User;
import java.util.UUID;

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
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return User.builder()
                .nickname("kakao#" + nickname + "_" + uniqueId)
                .imageUrl("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png")
                .build();
    }

    public static LoginInfo toKakaoLoginInfo(KakaoProfile kakaoProfile, User user){
        return LoginInfo.builder()
                .username("KAKAO_" + kakaoProfile.getId())
                .password("SOCIAL_LOGIN")
                .provider(Provider.KAKAO)
                .user(user)
                .build();
    }

    public static User naverToUser(NaverProfile naverProfile){
        String email = naverProfile.getNaverAccount().getEmail();
        String nickname = email.substring(0, email.indexOf('@'));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return User.builder()
                .nickname("naver#" + nickname + "_" + uniqueId)
                .imageUrl("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png")
                .build();
    }

    public static LoginInfo toNaverLoginInfo(NaverProfile naverProfile, User user){
        return LoginInfo.builder()
                .username("NAVER_" + naverProfile.getNaverAccount().getId())
                .password("SOCIAL_LOGIN")
                .provider(Provider.NAVER)
                .user(user)
                .build();
    }

    public static User googleToUser(GoogleProfile googleProfile){
        String email = googleProfile.getEmail();
        String nickname = email.substring(0, email.indexOf('@'));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return User.builder()
                .nickname("google#" + nickname + "_" + uniqueId)
                .imageUrl("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png")
                .build();
    }

    public static LoginInfo toGoogleLoginInfo(GoogleProfile googleProfile, User user){
        return LoginInfo.builder()
                .username("GOOGLE_" + googleProfile.getSub())
                .password("SOCIAL_LOGIN")
                .provider(Provider.GOOGLE)
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
