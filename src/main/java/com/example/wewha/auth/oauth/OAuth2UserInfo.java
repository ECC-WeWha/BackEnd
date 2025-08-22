package com.example.wewha.auth.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getProvider();     // google, kakao, naver
    public abstract String getProviderId();   // 각 플랫폼 유저 ID
    public abstract String getEmail();
    public abstract String getNickname();
    public abstract String getName();
}
