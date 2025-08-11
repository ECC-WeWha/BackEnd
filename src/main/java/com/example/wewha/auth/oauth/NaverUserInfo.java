package com.example.wewha.auth.oauth;

import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response")); // "response" 안에 실제 사용자 정보 있음
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("nickname").toString();
    }

    @Override
    public String getName() {
        // 네이버는 기본적으로 "name" 필드를 포함함
        return attributes.get("name").toString();
    }
}
