package com.example.springsecuritypractice.config.auth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;
    private Map<String,Object> kakaoAccountAttributes;
    private Map<String, Object> profileAttribute;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttribute = (Map<String, Object>) attributes.get("profile");
    }

    @Override
    public String getProvideId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccountAttributes.get("email");
    }

    @Override
    public String getName() {
        return (String) kakaoAccountAttributes.get("nickname");
    }
}
