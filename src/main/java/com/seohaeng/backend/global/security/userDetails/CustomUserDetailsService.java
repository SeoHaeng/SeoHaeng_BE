package com.seohaeng.backend.global.security.userDetails;

import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginInfoRepository loginInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginInfo loginInfo =
                loginInfoRepository.findByUsernameWithUser(username)
                .orElseThrow(() ->new UsernameNotFoundException("해당 username 가진 유저가 존재하지 않습니다."));

        User user = loginInfo.getUser();

        return new LocalCustomUserDetails(user, loginInfo);
    }
}