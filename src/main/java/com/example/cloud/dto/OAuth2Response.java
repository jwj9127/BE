package com.example.cloud.dto;

public interface OAuth2Response {
    // 제공자 : naver, google, kakao, ..등
    String getProvider();

    // 제공자에게서 발급해주는 아이디 (번호)
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 이름
    String getName();
}
