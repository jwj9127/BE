package com.example.cloud.oauth2;

import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KakaoProperties {
    @Value("${kakao_key}")
    private String clientId;

    @Value("${redirect_url}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;
}
