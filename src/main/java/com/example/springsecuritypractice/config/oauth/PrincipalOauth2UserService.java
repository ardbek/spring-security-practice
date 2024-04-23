package com.example.springsecuritypractice.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능함.
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리가 받아줌) -> AccessToken 요청
        // userRequest정보 -> 회원프로필 받아야함(loadUser함수) -> 회원프로필
        System.out.println("super.loadUser(userRequest) : " + super.loadUser(userRequest).getAttributes());

        // 회원가입 강제로 진행
        return super.loadUser(userRequest);
    }
}
