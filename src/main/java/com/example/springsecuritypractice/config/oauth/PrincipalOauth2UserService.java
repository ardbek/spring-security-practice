package com.example.springsecuritypractice.config.oauth;

import com.example.springsecuritypractice.config.auth.PrincipalDetails;
import com.example.springsecuritypractice.config.auth.provider.FacebookUserInfo;
import com.example.springsecuritypractice.config.auth.provider.GoogleUserInfo;
import com.example.springsecuritypractice.config.auth.provider.NaverUserInfo;
import com.example.springsecuritypractice.config.auth.provider.OAuth2UserInfo;
import com.example.springsecuritypractice.model.User;
import com.example.springsecuritypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능함.
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리가 받아줌) -> AccessToken 요청
        // userRequest정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아줌.
        System.out.println("getAttributes : " + oauth2User.getAttributes());


        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
        }  else {
            System.out.println("우리는 구글이나 페이스북과 네이버 지원해요ㅠㅠ");
        }


        // 회원가입 강제로 진행
        String provider = oAuth2UserInfo.getProvider(); // google
        String providerId = oAuth2UserInfo.getProvideId();
        String username = provider + "_" + providerId; //google_1231526377124
        String password = ("tempPassword"); //pwd는 의미 없음 어차피 구글의 인증 서버를 통해 인증하므로
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        // 이미 회원가입이 되어있는지 확인
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) { // 가입 x
            System.out.println("OAuth 로그인이 최초입니다.");
            userEntity = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("OAuth 로그인을 한적이 있습니다. 자동 회원가입이 되어있습니다.");
        }

        // PrincipalDetails 타입을 반환하기 위해 구현함
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
