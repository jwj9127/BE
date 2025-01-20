package com.example.cloud.oauth2;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

@Getter
public class KakaoProperties {
    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.token.url}")
    private String tokenUrl;

    @Value("${kakao.userinfo.url}")
    private String userInfoUrl;
}
