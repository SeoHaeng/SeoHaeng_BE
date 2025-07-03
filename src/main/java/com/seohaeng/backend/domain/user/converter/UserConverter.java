package com.seohaeng.backend.domain.user.converter;

import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.Provider;
import com.seohaeng.backend.domain.user.entity.User;

public class UserConverter {

    public static User toLocalUser(UserRequestDTO.joinDTO request){
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
}
