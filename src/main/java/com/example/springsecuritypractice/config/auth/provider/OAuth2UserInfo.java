package com.example.springsecuritypractice.config.auth.provider;

public interface OAuth2UserInfo {
    String getProvideId();
    String getProvider();
    String getEmail();
    String getName();
}
