package com.example.wewha.auth.oauth;

import com.example.wewha.auth.oauth.GoogleUserInfo;
import com.example.wewha.auth.oauth.KakaoUserInfo;
import com.example.wewha.auth.oauth.NaverUserInfo;
import com.example.wewha.auth.oauth.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
            case "naver" -> new NaverUserInfo(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인 서비스입니다: " + registrationId);
        };
    }
}
