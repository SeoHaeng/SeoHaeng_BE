package com.seohaeng.backend.global.security.authProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seohaeng.backend.domain.user.dto.GoogleProfile;
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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Getter
public class GoogleAuthProvider {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String client;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirect;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String secret;

    public OAuthToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type"
                ,"application/x-www-form-urlencoded;charset=utf-8");

        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirect);
        params.add("client_secret", secret);
        params.add("code", decode);

        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://oauth2.googleapis.com/token",
                        HttpMethod.POST,
                        googleTokenRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        return oAuthToken;
    }

    public GoogleProfile requestGooglerofile(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://www.googleapis.com/oauth2/v3/userinfo",
                        HttpMethod.GET,
                        googleProfileRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile = null;

        try {
            googleProfile = objectMapper.readValue(response.getBody(), GoogleProfile.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO_GOOGLE);
        }

        return googleProfile;
    }
}
