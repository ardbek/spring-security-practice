package com.example.springsecuritypractice.config.oauth;

import com.example.springsecuritypractice.config.auth.PrincipalDetails;
import com.example.springsecuritypractice.model.User;
import com.example.springsecuritypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능함.
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oauth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리가 받아줌) -> AccessToken 요청
        // userRequest정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아줌.
        System.out.println("getAttributes : " + oauth2User.getAttributes());

        // 회원가입 강제로 진행
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_" + providerId; //google_1231526377124
        String password = ("tempPassword"); //pwd는 의미 없음 어차피 구글의 인증 서버를 통해 인증하므로
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";

        // 이미 회원가입이 되어있는지 확인
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) { // 가입 x
            System.out.println("구글 로그인이 최초입니다.");
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
            System.out.println("이미 구글로그인을 한적이 있습니다. 자동 회원가입이 되어있습니다.");
        }

        // PrincipalDetails 타입을 반환하기 위해 구현함
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
