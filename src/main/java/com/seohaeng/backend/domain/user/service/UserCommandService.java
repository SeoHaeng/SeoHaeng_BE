package com.seohaeng.backend.domain.user.service;

import com.seohaeng.backend.domain.user.converter.UserConverter;
import com.seohaeng.backend.domain.user.dto.KakaoProfile;
import com.seohaeng.backend.domain.user.dto.OAuthToken;
import com.seohaeng.backend.domain.user.dto.UserRequestDTO;
import com.seohaeng.backend.domain.user.dto.UserResponseDTO;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.Provider;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.AuthException;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import com.seohaeng.backend.global.security.KakaoAuthProvider;
import com.seohaeng.backend.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.seohaeng.backend.domain.user.converter.UserConverter.toLocalLoginInfo;
import static com.seohaeng.backend.domain.user.converter.UserConverter.toUserInfoDTO;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final PasswordEncoder passwordEncoder;
    private final LoginInfoRepository loginInfoRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthProvider kakaoAuthProvider;
    private final AmazonS3Manager amazonS3Manager;

    // 회원가입
    @Transactional
    public LoginInfo joinUser(UserRequestDTO.joinDTO joinDTO) {

        if (!Objects.equals(joinDTO.getPassword1(), joinDTO.getPassword2())) {
            throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
        }

        validatePasswordComplexity(joinDTO.getPassword1());

        if (loginInfoRepository.existsByUsername(joinDTO.getUsername())) {
            throw new UserHandler(ErrorStatus.DUPLICATE_USERNAME);
        }

        boolean exists = userRepository.existsByNickname(joinDTO.getNickname());
        if (exists){
            throw new AuthException(ErrorStatus.DUPLICATE_NICKNAME);
        }

        User joinUser = UserConverter.toUser(joinDTO);
        userRepository.save(joinUser);

        String encodedPassword = passwordEncoder.encode(joinDTO.getPassword1());
        LoginInfo joinLoginInfo = toLocalLoginInfo(joinDTO, joinUser);
        joinLoginInfo.encodePassword(encodedPassword);

        return loginInfoRepository.save(joinLoginInfo);
    }

    // 일반 로그인
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

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return UserConverter.toLoginResultDTO(
                loginUser.getId(),
                accessToken,
                refreshToken
        );
    }

    // 카카오 로그인
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

        return getOauthResponseForUser(loginInfo);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId, UserRequestDTO.DeleteAccountDTO password){

        User user = userRepository.findUserWithLoginInfoById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        LoginInfo loginInfo = user.getLoginInfo();
        Provider provider = loginInfo.getProvider();

        if(passwordEncoder.matches(password.getPassword(), loginInfo.getPassword())){
            if(provider.equals(Provider.LOCAL)){
                userRepository.delete(user);
            } // TODO : 각 소셜 로그인 Provider 별로 처리
        }else{
            throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
        }
    }


    // 사용자 정보 변경
    @Transactional
    public UserResponseDTO.GetUserInfoResponseDTO updateUserInfo(
            Long userId,
            UserRequestDTO.updateProfileDTO request,
            Boolean useDefault,
            MultipartFile image){

        User user = userRepository.findUserWithLoginInfoById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        LoginInfo loginInfo = user.getLoginInfo();

        if(loginInfo.getProvider() != Provider.LOCAL){
            throw new UserHandler(ErrorStatus._FORBIDDEN);
        }

        if(request.getUsername() != null){
            if (!Objects.equals(loginInfo.getUsername(), request.getUsername())) {
                if(loginInfoRepository.existsByUsername(request.getUsername())){
                    throw new UserHandler(ErrorStatus.DUPLICATE_USERNAME);
                }
                loginInfo.setUsername(request.getUsername());
            }
        } // 아이디 변경

        if(request.getNickname() != null){
            if (!Objects.equals(user.getNickname(), request.getNickname())) {
                boolean exists = userRepository.existsByNickname(request.getNickname());
                if (exists){
                    throw new AuthException(ErrorStatus.DUPLICATE_NICKNAME);
                }
                user.setNickname(request.getNickname());
            }
        } // 닉네임 변경

        if(request.getPassword1() != null){
            if (!Objects.equals(request.getPassword1(), request.getPassword2())) {
                throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
            }
            validatePasswordComplexity(request.getPassword1());
            String encodedPassword = passwordEncoder.encode(request.getPassword1());
            loginInfo.setPassword(encodedPassword);
        } // 비밀번호 변경

        if (Boolean.TRUE.equals(useDefault)) {
            user.setImageUrl("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/profiles/default_profile.png");
        } else if (Boolean.FALSE.equals(useDefault) && image != null && !image.isEmpty()) {
            final String uuid = UUID.randomUUID().toString();
            final String keyName = amazonS3Manager.generateProfileKeyName(uuid);
            final String imageUrl = amazonS3Manager.uploadFile(keyName, image);
            user.setImageUrl(imageUrl);
        } // 프로필 이미지 변경

        return toUserInfoDTO(user);
    }

    private void validatePasswordComplexity(String password) {
        String pattern = "^(?!.*[가-힣])(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$";

        if (!password.matches(pattern)) {
            throw new UserHandler(ErrorStatus.PASSWORD_COMPLEXITY_FAIL);
        }
    }

    private UserResponseDTO.LoginResultDTO getOauthResponseForUser(LoginInfo logininfo) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                logininfo.getUsername(),
                null,
                Collections.emptyList());

        User loginUser = logininfo.getUser();

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        return UserConverter.toLoginResultDTO(loginUser.getId(),accessToken,refreshToken);
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