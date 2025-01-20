package com.example.cloud.dto;

import java.util.Map;

public class OAuth2NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    // 네이버는 응답 JSON 형태에 사용자 데이터가 response에 따로 싸여서 담겨있어서 기본 생성자(@RequiredArgsConstructor) 안쓰고 response를 따로 찾아 뜯어내 생성자주입.
    public OAuth2NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
