package com.seohaeng.backend.domain.user.service;

import com.seohaeng.backend.domain.travelCourse.converter.TravleCourseConverter;
import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
import com.seohaeng.backend.domain.travelCourse.repository.StampRepository;
import com.seohaeng.backend.domain.user.converter.UserConverter;
import com.seohaeng.backend.domain.user.dto.KakaoProfile;
import com.seohaeng.backend.domain.user.dto.OAuthToken;
import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.AuthException;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.security.KakaoAuthProvider;
import com.seohaeng.backend.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.seohaeng.backend.domain.user.converter.UserConverter.toLocalLoginInfo;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final PasswordEncoder passwordEncoder;
    private final LoginInfoRepository loginInfoRepository;
    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthProvider kakaoAuthProvider;

    @Transactional
    public LoginInfo joinUser(UserRequestDTO.joinDTO joinDTO) {

        if (!Objects.equals(joinDTO.getPassword1(), joinDTO.getPassword2())) {
            throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
        }

        validatePasswordComplexity(joinDTO.getPassword1());

        if (loginInfoRepository.existsByUsername(joinDTO.getUsername())) {
            throw new UserHandler(ErrorStatus.DUPLICATE_USERNAME);
        }

        User joinUser = UserConverter.toUser(joinDTO);
        userRepository.save(joinUser);

        Stamp joinUserStamp = TravleCourseConverter.toStamp(joinUser);
        stampRepository.save(joinUserStamp);

        String encodedPassword = passwordEncoder.encode(joinDTO.getPassword1());
        LoginInfo joinLoginInfo = toLocalLoginInfo(joinDTO, joinUser);
        joinLoginInfo.encodePassword(encodedPassword);

        return loginInfoRepository.save(joinLoginInfo);
    }

    public UserResponseDTO.LoginResultDTO loginUser(UserRequestDTO.LoginDTO loginDTO) {

        LoginInfo loginUserLoginInfo = loginInfoRepository.findByUsernameWithUser(loginDTO.getUsername())
                .orElseThrow(() -> new UserHandler(ErrorStatus.LOGIN_INFO_NOT_FOUND));

        User loginUser = loginUserLoginInfo.getUser();

        if(!passwordEncoder.matches(loginDTO.getPassword(), loginUserLoginInfo.getPassword())){
            throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUserLoginInfo.getUsername(),
                null,
                Collections.emptyList());

        String accessToken = jwtTokenProvider.generateToken(authentication);

        return UserConverter.toLoginResultDTO(
                loginUser.getId(),
                accessToken
        );
    }

    private void validatePasswordComplexity(String password) {
        String pattern = "^(?!.*[가-힣])(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$";

        if (!password.matches(pattern)) {
            throw new UserHandler(ErrorStatus.PASSWORD_COMPLEXITY_FAIL);
        }
    }

    @Transactional
    public UserResponseDTO.LoginResultDTO kakaoLogin(String code) {
        OAuthToken oAuthToken = getKakaoOauthToken(code);

        KakaoProfile kakaoProfile;
        try {
            kakaoProfile = kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccess_token());
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_KAKAO);
        }

        Optional<LoginInfo> userLoginInfo = loginInfoRepository.findByUsername(kakaoProfile.getKakaoAccount().getEmail());

        if (userLoginInfo.isPresent()) {
            LoginInfo logininfo = userLoginInfo.get();
            return getOauthResponseForUser(logininfo);
        }

        User user = userRepository.save(UserConverter.kakaoToUser(kakaoProfile));
        LoginInfo loginInfo = loginInfoRepository.save(UserConverter.toKakaoLoginInfo(kakaoProfile,user));
        Stamp joinUserStamp = TravleCourseConverter.toStamp(user);
        stampRepository.save(joinUserStamp);

        return getOauthResponseForUser(loginInfo);
    }

    private UserResponseDTO.LoginResultDTO getOauthResponseForUser(LoginInfo logininfo) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                logininfo.getUsername(),
                null,
                Collections.emptyList());

        User loginUser = logininfo.getUser();

        String accessToken = jwtTokenProvider.generateToken(authentication);
        return UserConverter.toLoginResultDTO(loginUser.getId(),accessToken);
    }

    private OAuthToken getKakaoOauthToken(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = kakaoAuthProvider.requestToken(code);
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }
        return oAuthToken;
    }
}