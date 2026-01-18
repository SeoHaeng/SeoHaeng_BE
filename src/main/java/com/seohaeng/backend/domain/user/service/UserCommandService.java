package com.seohaeng.backend.domain.user.service;

import com.seohaeng.backend.domain.user.converter.UserConverter;
import com.seohaeng.backend.domain.user.dto.*;
import com.seohaeng.backend.domain.user.entity.Agreement;
import com.seohaeng.backend.domain.user.entity.LoginInfo;
import com.seohaeng.backend.domain.user.entity.Provider;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.AgreementRepository;
import com.seohaeng.backend.domain.user.repository.LoginInfoRepository;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.AuthException;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import com.seohaeng.backend.global.security.authProvider.GoogleAuthProvider;
import com.seohaeng.backend.global.security.authProvider.KakaoAuthProvider;
import com.seohaeng.backend.global.security.authProvider.NaverAuthProvider;
import com.seohaeng.backend.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

import java.util.*;

import static com.seohaeng.backend.domain.user.converter.UserConverter.toLocalLoginInfo;
import static com.seohaeng.backend.domain.user.converter.UserConverter.toUserInfoDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final PasswordEncoder passwordEncoder;
    private final LoginInfoRepository loginInfoRepository;
    private final UserRepository userRepository;
    private final AgreementRepository agreementRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KakaoAuthProvider kakaoAuthProvider;
    private final NaverAuthProvider naverAuthProvider;
    private final GoogleAuthProvider googleAuthProvider;
    private final AmazonS3Manager amazonS3Manager;

    @Value("${jwt.token.expiration.refresh}")
    private long refreshTokenExpiration;

    // 토큰 재발급
    public UserResponseDTO.TokenResponse reissueToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.extractToken(request);

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String username = authentication.getName();

        LoginInfo loginInfo = loginInfoRepository.findByUsernameWithUser(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        User user = loginInfo.getUser();
        Long userId = user.getId();

        String redisKey = "refreshToken:" + userId;
        String storedRefreshToken = (String) redisTemplate.opsForValue().get(redisKey);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        saveRefreshTokenToRedis(userId, newRefreshToken);

        return UserResponseDTO.TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

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

        agreementRepository.save(
                Agreement.builder()
                .user(joinUser)
                .termsOfServiceAgreed(joinDTO.getTermsOfServiceAgreed())
                .privacyPolicyAgreed(joinDTO.getPrivacyPolicyAgreed())
                .locationServiceAgreed(joinDTO.getLocationServiceAgreed())
                .build()
        );

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

        Agreement agreement = agreementRepository.findByUser(loginUser)
                .orElseThrow(() -> new UserHandler(ErrorStatus.AGREEMENT_NOT_FOUND));

        if (!agreement.getTermsOfServiceAgreed() || !agreement.getPrivacyPolicyAgreed() || !agreement.getLocationServiceAgreed()) {
            throw new UserHandler(ErrorStatus.AGREEMENT_NOT_COMPLETED);
        }

        if(!passwordEncoder.matches(loginDTO.getPassword(), loginUserLoginInfo.getPassword())){
            throw new UserHandler(ErrorStatus.PASSWORD_MISMATCH);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUserLoginInfo.getUsername(),
                null,
                Collections.emptyList());

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        saveRefreshTokenToRedis(loginUser.getId(), refreshToken);

        return UserConverter.toLoginResultDTO(
                loginUser.getId(),
                accessToken,
                refreshToken,
                false
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

        Optional<LoginInfo> userLoginInfo = loginInfoRepository.findByUsername("KAKAO_" + kakaoProfile.getId());

        if (userLoginInfo.isPresent()) {
            LoginInfo logininfo = userLoginInfo.get();
            return getOauthResponseForUser(logininfo, false);
        }

        User user = userRepository.save(UserConverter.kakaoToUser(kakaoProfile));
        LoginInfo loginInfo = loginInfoRepository.save(UserConverter.toKakaoLoginInfo(kakaoProfile,user));

        return getOauthResponseForUser(loginInfo, true);
    }

    // 네이버 로그인
    @Transactional
    public UserResponseDTO.LoginResultDTO naverLogin(String code) {
        OAuthToken oAuthToken = getNaverOauthToken(code);

        NaverProfile naverProfile;
        try {
            naverProfile = naverAuthProvider.requestNaverProfile(oAuthToken.getAccess_token());
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_NAVER);
        }

        Optional<LoginInfo> userLoginInfo = loginInfoRepository.findByUsername("NAVER_" + naverProfile.getNaverAccount().getId());

        if (userLoginInfo.isPresent()) {
            LoginInfo logininfo = userLoginInfo.get();
            return getOauthResponseForUser(logininfo, false);
        }

        User user = userRepository.save(UserConverter.naverToUser(naverProfile));
        LoginInfo loginInfo = loginInfoRepository.save(UserConverter.toNaverLoginInfo(naverProfile,user));

        return getOauthResponseForUser(loginInfo, true);
    }

    // 구글 로그인
    @Transactional
    public UserResponseDTO.LoginResultDTO googleLogin(String code) {
        OAuthToken oAuthToken = getGoogleOauthToken(code);

        GoogleProfile googleProfile;
        try {
            googleProfile = googleAuthProvider.requestGooglerofile(oAuthToken.getAccess_token());
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        Optional<LoginInfo> userLoginInfo = loginInfoRepository.findByUsername("GOOGLE_" + googleProfile.getSub());

        if (userLoginInfo.isPresent()) {
            LoginInfo logininfo = userLoginInfo.get();
            return getOauthResponseForUser(logininfo, false);
        }

        User user = userRepository.save(UserConverter.googleToUser(googleProfile));
        LoginInfo loginInfo = loginInfoRepository.save(UserConverter.toGoogleLoginInfo(googleProfile,user));

        return getOauthResponseForUser(loginInfo, true);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId){

        User user = userRepository.findUserWithLoginInfoById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    // 로그아웃
    @Transactional
    public void logout(Long userId, String token){

        User user = userRepository.findUserWithLoginInfoById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        blackListAccessToken(token);
        deleteRefreshTokenToRedis(userId);
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

        if(request.getUsername() != null){
            if(loginInfo.getProvider() != Provider.LOCAL){
                throw new UserHandler(ErrorStatus._FORBIDDEN);
            }
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
            if(loginInfo.getProvider() != Provider.LOCAL){
                throw new UserHandler(ErrorStatus._FORBIDDEN);
            }
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

    // 약관 동의
    @Transactional
    public UserResponseDTO.AgreementResponse saveAgreement(Long userId, UserRequestDTO.AgreementRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Agreement> UserAgreement = agreementRepository.findByUser(user);
        if(UserAgreement.isPresent()){
            throw new UserHandler(ErrorStatus.AGREEMENT_ALREADY_EXISTS);
        }

        Agreement agreement = Agreement.builder()
                .user(user)
                .termsOfServiceAgreed(request.getTermsOfServiceAgreed())
                .privacyPolicyAgreed(request.getPrivacyPolicyAgreed())
                .locationServiceAgreed(request.getLocationServiceAgreed())
                .build();

        Agreement savedAgreement = agreementRepository.save(agreement);

        return UserResponseDTO.AgreementResponse.builder()
                .agreementId(savedAgreement.getId())
                .userId(user.getId())
                .termsOfServiceAgreed(savedAgreement.getTermsOfServiceAgreed())
                .privacyPolicyAgreed(savedAgreement.getPrivacyPolicyAgreed())
                .locationServiceAgreed(savedAgreement.getLocationServiceAgreed())
                .build();
    }

    private void saveRefreshTokenToRedis(Long userId, String refreshToken) {
        String redisKey = "refreshToken:" + userId;
        redisTemplate.opsForValue().set(redisKey, refreshToken, Duration.ofMillis(refreshTokenExpiration));
    }

    private void deleteRefreshTokenToRedis(Long userId) {
        String redisKey = "refreshToken:" + userId;
        redisTemplate.delete(redisKey);
    }

    private void blackListAccessToken(String token) {
        long remainMillis = jwtTokenProvider.getRemainingTimeFromToken(token);
        String redisKey = "blacklist:access-token:" + token;
        redisTemplate.opsForValue().set(redisKey, token, Duration.ofMillis(remainMillis));
    }

    private void validatePasswordComplexity(String password) {
        String pattern = "^(?!.*[가-힣])(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$";

        if (!password.matches(pattern)) {
            throw new UserHandler(ErrorStatus.PASSWORD_COMPLEXITY_FAIL);
        }
    }

    private UserResponseDTO.LoginResultDTO getOauthResponseForUser(LoginInfo logininfo, Boolean isNewUser) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                logininfo.getUsername(),
                null,
                Collections.emptyList());

        User loginUser = logininfo.getUser();

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        saveRefreshTokenToRedis(loginUser.getId(), refreshToken);

        return UserConverter.toLoginResultDTO(loginUser.getId(),accessToken,refreshToken, isNewUser);
    }

    private OAuthToken getKakaoOauthToken(String code) {
        OAuthToken oAuthToken;
        try {
            log.info("[KAKAO LOGIN] 인가코드로 토큰 요청 시작: code={}", code);
            oAuthToken = kakaoAuthProvider.requestToken(code);
            log.info("[KAKAO LOGIN] 토큰 요청 성공");
        } catch (Exception e) {
            log.error("[KAKAO LOGIN] 토큰 요청 실패: code={}, error={}", code, e.getMessage(), e);
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }
        return oAuthToken;
    }

    private OAuthToken getNaverOauthToken(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = naverAuthProvider.requestToken(code);
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }
        return oAuthToken;
    }

    private OAuthToken getGoogleOauthToken(String code) {
        OAuthToken oAuthToken;
        try {
            oAuthToken = googleAuthProvider.requestToken(code);
        } catch (Exception e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_CODE);
        }
        return oAuthToken;
    }

    public enum UserStatusDTO{
        NEW_USER,
        EXISTING_USER
    }
}