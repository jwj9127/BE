package com.example.cloud.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoProperties kakaoProperties;

    public String getAccessToken(String code) {
        // 토큰 요청 URL
        String url = UriComponentsBuilder.fromHttpUrl(kakaoProperties.getTokenUrl())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .queryParam("code", code)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        String accessToken = extractAccessToken(response.getBody());
        return accessToken;
    }

    private String extractAccessToken(String responseBody) {
        String accessToken = responseBody.substring(responseBody.indexOf("access_token\":\"") + 15);
        accessToken = accessToken.substring(0, accessToken.indexOf("\""));
        return accessToken;
    }

    public String getUserInfo(String accessToken) {
        // 카카오 사용자 정보 요청 URL
        String url = UriComponentsBuilder.fromHttpUrl(kakaoProperties.getUserInfoUrl())
                .toUriString();

        // HttpHeaders 객체 생성 후 Authorization 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // HttpEntity 객체에 헤더 포함
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate을 이용해 GET 요청을 보내 사용자 정보를 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 응답에서 사용자 정보 추출
        String userInfo = response.getBody();
        return userInfo;
    }

}
