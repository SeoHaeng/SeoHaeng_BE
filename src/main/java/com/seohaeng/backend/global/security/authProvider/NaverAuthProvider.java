package com.seohaeng.backend.global.security.authProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seohaeng.backend.domain.user.dto.NaverProfile;
import com.seohaeng.backend.domain.user.dto.OAuthToken;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.AuthException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Getter
public class NaverAuthProvider {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String client;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String secret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirect;

    public OAuthToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type"
                ,"application/x-www-form-urlencoded;charset=utf-8");

        String state = UUID.randomUUID().toString();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("client_secret", secret);
        params.add("redirect_uri", redirect);
        params.add("state","test");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://nid.naver.com/oauth2.0/token",
                        HttpMethod.POST,
                        naverTokenRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_KAKAO);
        }
        return oAuthToken;
    }

    public NaverProfile requestNaverProfile(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://openapi.naver.com/v1/nid/me",
                        HttpMethod.GET,
                        naverProfileRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile = null;
        System.out.println(response.getBody());
        try {
            naverProfile = objectMapper.readValue(response.getBody(), NaverProfile.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_NAVER);
        }
        System.out.println(naverProfile.getNaverAccount().getEmail());
        return naverProfile;
    }
}
