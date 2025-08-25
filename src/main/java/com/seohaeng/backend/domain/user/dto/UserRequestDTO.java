package com.seohaeng.backend.domain.user.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    public static class joinDTO{

        @NotBlank
        @Size(min = 4, max = 12)
        String username;

        @NotBlank
        String nickname;

        @NotBlank
        @Size(min = 8, max = 20)
        String password1;

        @NotBlank
        @Size(min = 8, max = 20)
        String password2;
    }

    @Getter
    @Setter
    public static class LoginDTO{
        @NotBlank(message = "아이디는 필수입니다.")
        private String username;

        @NotBlank(message = "패스워드는 필수입니다.")
        private String password;
    }

    @Getter
    public static class updateProfileDTO{

        @Size(min = 4, max = 12)
        private String username;

        private String nickname;

        @Size(min = 8, max = 20)
        private String password1;

        @Size(min = 8, max = 20)
        private String password2;
    }

    @Getter
    public static class AgreementRequestDTO {

        // 이용약관 동의
        @AssertTrue(message = "이용약관에 필수로 동의해야 합니다.")
        private Boolean termsOfServiceAgreed;

        // 개인정보 처리방침 동의
        @AssertTrue(message = "개인정보 처리방침에 필수로 동의해야 합니다.")
        private Boolean privacyPolicyAgreed;
    }
}