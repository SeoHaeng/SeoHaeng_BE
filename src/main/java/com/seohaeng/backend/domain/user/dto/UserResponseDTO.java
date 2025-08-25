package com.seohaeng.backend.domain.user.dto;

import com.seohaeng.backend.domain.user.entity.Provider;
import lombok.*;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResultDTO {
        Long UserId;
        String accessToken;
        String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserInfoResponseDTO {
        Long userId;
        String nickName;
        String profileImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyInfoResponseDTO {
        Long userId;
        String userName;
        String nickName;
        String profileImageUrl;
        Provider loginType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {
        String accessToken;
        String refreshToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreementResponse {
        Long agreementId;
        Long userId;
        Boolean termsOfServiceAgreed;
        Boolean privacyPolicyAgreed;
    }
}
