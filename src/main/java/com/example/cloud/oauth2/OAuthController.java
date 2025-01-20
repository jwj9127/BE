//package com.example.cloud.oauth2;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/oauth")
//public class OAuthController {
//
//    private final KakaoProperties kakaoProperties;
//
//    private final OAuthService oAuthService;
//
//    @GetMapping(value="/kakao")
//    public String kakaoConnect() {
//        StringBuffer url = new StringBuffer();
//        url.append("https://kauth.kakao.com/oauth/authorize?");
//        url.append("client_id="+kakaoProperties.getClientId());
//        url.append("&redirect_uri="+kakaoProperties.getRedirectUri());
//        url.append("&response_type=code");
//        return "redirect:" + url.toString();
//    }
//
//    @GetMapping("/kakao/callback")
//    public String kakaoCallback(String code, HttpSession session) {
//
//        // SETP1 : 인가코드 받기
//        // (카카오 인증 서버는 서비스 서버의 Redirect URI로 인가 코드를 전달합니다.)
//        // System.out.println(code);
//
//        // STEP2: 인가코드를 기반으로 토큰(Access Token) 발급
//        String accessToken = null;
//        try {
//            accessToken = oAuthService.getAccessToken(code);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        //System.out.println("엑세스 토큰  "+accessToken);
//
//        // STEP3: 토큰를 통해 사용자 정보 조회
//        KakaoInfo kakaoInfo = null;
//        try {
//            kakaoInfo = oAuthService.getKakaoInfo(accessToken);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        //System.out.println("이메일 확인 "+kakaoInfo.getEmail());
//
//        // STEP4: 카카오 사용자 정보 확인
//        MemberResponse kakaoMember = oAuthService.ifNeedKakaoInfo(kakaoInfo);
//
//        // STEP5: 강제 로그인
//        // 세션에 회원 정보 저장 & 세션 유지 시간 설정
//        if (kakaoMember != null) {
//            session.setAttribute("loginMember", kakaoMember);
//            // session.setMaxInactiveInterval( ) : 세션 타임아웃을 설정하는 메서드
//            // 로그인 유지 시간 설정 (1800초 == 30분)
//            session.setMaxInactiveInterval(60 * 30);
//            // 로그아웃 시 사용할 카카오토큰 추가
//            session.setAttribute("kakaoToken", accessToken);
//        }
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/kakao/logout")
//    public String kakaoLogout(HttpSession session) {
//        String accessToken = (String) session.getAttribute("kakaoToken");
//
//        if(accessToken != null && !"".equals(accessToken)){
//            try {
//                oAuthService.kakaoDisconnect(accessToken);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            session.removeAttribute("kakaoToken");
//            session.removeAttribute("loginMember");
//        }else{
//            System.out.println("accessToken is null");
//        }
//
//        return "redirect:/";
//    }
//
//}
