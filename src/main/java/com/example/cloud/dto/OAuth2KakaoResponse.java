package com.example.cloud.dto;

import java.util.Map;

public class OAuth2KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attributes;

    public OAuth2KakaoResponse(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        // "kakao_account"에서 "email" 키 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            Object email = kakaoAccount.get("email");
            if (email != null) {
                return email.toString();
            }
        }
        return "Unknown"; // 기본값
    }

    @Override
    public String getName() {
        // "kakao_account" -> "profile"에서 "nickname" 키 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                Object nickname = profile.get("nickname");
                if (nickname != null) {
                    return nickname.toString();
                }
            }
        }
        return "Unknown"; // 기본값
    }

}