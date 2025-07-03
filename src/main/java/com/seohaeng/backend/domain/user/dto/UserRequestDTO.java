package com.seohaeng.backend.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class joinDTO{

        @NotNull
        @Size(min = 4, max = 12)
        String username;

        @NotNull
        String nickname;

        @NotNull
        @Size(min = 8, max = 20)
        String password1;

        @NotNull
        @Size(min = 8, max = 20)
        String password2;
    }
}