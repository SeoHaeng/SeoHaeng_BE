package com.seohaeng.backend.domain.user.service;

import com.seohaeng.backend.domain.user.converter.UserConverter;
import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.seohaeng.backend.domain.user.converter.UserConverter.toLocalLoginInfo;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final PasswordEncoder passwordEncoder;
    private final LoginInfoRepository loginInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public LoginInfo joinUser(UserRequestDTO.joinDTO joinDTO) {

        if (!Objects.equals(joinDTO.getPassword1(), joinDTO.getPassword2())) {
            throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
        }

        validatePasswordComplexity(joinDTO.getPassword1());

        if (loginInfoRepository.existsByUsername(joinDTO.getUsername())) {
            throw new UserHandler(ErrorStatus.DUPLICATE_USERNAME);
        }

        User joinUser = UserConverter.toLocalUser(joinDTO);
        userRepository.save(joinUser);

        String encodedPassword = passwordEncoder.encode(joinDTO.getPassword1());
        LoginInfo joinLoginInfo = toLocalLoginInfo(joinDTO, joinUser);
        joinLoginInfo.encodePassword(encodedPassword);

        return loginInfoRepository.save(joinLoginInfo);
    }

    private void validatePasswordComplexity(String password) {
        String pattern = "^(?!.*[가-힣])(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$";

        if (!password.matches(pattern)) {
            throw new UserHandler(ErrorStatus.PASSWORD_COMPLEXITY_FAIL);
        }
    }
}
