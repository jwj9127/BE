package com.example.cloud.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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

    public KakaoInfo getKakaoInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        return new KakaoInfo(id, nickname);
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
