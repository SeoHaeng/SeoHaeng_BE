package com.seohaeng.backend.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverProfile {

    @JsonProperty("response")
    private NaverAccount naverAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaverAccount {
        private String email;
    }
}
