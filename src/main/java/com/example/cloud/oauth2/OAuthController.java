package com.example.cloud.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoProperties kakaoProperties;

    private final OAuthService oAuthService;

    @GetMapping(value="/kakao")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoProperties.getClientId());
        url.append("&redirect_uri="+kakaoProperties.getRedirectUri());
        return "redirect:" + url.toString();
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoRedirect(@RequestParam("code") String code) {
        // Step 1: authorization code를 사용해 access token을 요청
        String accessToken = oAuthService.getAccessToken(code);

        // Step 2: access token을 사용하여 카카오 사용자 정보를 요청
        String userInfo = oAuthService.getUserInfo(accessToken);

        // Step 3: 사용자 정보 처리 (예: 회원가입/로그인 처리)
        return "User Info: " + userInfo;
    }

}
