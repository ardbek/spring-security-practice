package com.example.springsecuritypractice.controller;

import com.example.springsecuritypractice.config.auth.PrincipalDetails;
import com.example.springsecuritypractice.model.User;
import com.example.springsecuritypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails) { // DI(의존성 주입)
        System.out.println("/test/login =========================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getUser().getUsername());

        return "세션 정보 확인하기";
    }

    // 스프링 시큐리티는 자기만의 세션을 가지고 있음 (Security Session)
    // 원래 서버에서 가지고 있는 세션과는 다른 세션
    // Security Sessoin 에 들어갈 수 있는 타입은 Authentication 객체만 들어갈 수 있음.
    // Authentication 객체에는 1.UserDetails 타입과 2.OAuth2User 타입만 들어갈 수 있음.
    // UserDtails 객체는 일반 로그인 할 경우 생성, OAuth2User 객체는 OAuth 로그인 할 경우 생성되어 Authenticatoin 객체에 들어감
    // 필요할 때 꺼내써야 하는데 불편한 점이 있음.
    // 일반로그인 할 경우 세션을 유지하려면 @AuthenticationPrincipal PrincipalDetails userDetails 로 받아야됨.
    // 반면 소셜 로그인 할 경우 @AuthenticationPrincipal OAuth2User oauth 로 받아야됨.
    // 이를 해결하기 위해서는 클래스를 하나 생성해서 UserDetails를 상속받고, OAuth2User도 상속받으면 x라는 클래스는 UserDetails도 받을 수 있고, OAuth2User 도 받을 수 있음
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
        System.out.println("/test/oauth/login =========================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        System.out.println("oauth = " + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    /**
     * 인덱스 페이지
     * @return
     */
    @GetMapping({"","/"})
    public String index() {
        //머스테치 기본 폴더 src/main/resources/
        // viewResolver : templates(prefix), .mustache (suffix) 생략가능
        return "index"; // src/main/resources/templates/index.mustache
    }

    /**
     * 사용자
     * @return
     */
    // OAuth 로그인 해도 PrincipalDetails 로 받을 수 있고,
    // 일반 로그인을 해도 PrincipalDetails 로 받을 수 있음.
    // @AuthenticationPrincipal은
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("/user :: principalDetails = " + principalDetails.getUser());
        return "user";
    }

    /**
     * 관리자
     * @return
     */
    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    /**
     * 매니저
     * @return
     */
    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    /**
     * 로그인 (spring security가 기본 설정된 페이지로 이동키기 때문에 SecurityConfig에서 설정)
     * @return
     */
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    /**
     * 회원가입 form
     * @return
     */
    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }
    
    /**
     * 회원가입
     */
    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입, 비밀번호 암호화 필요 => security로 로그인 할 수 없음
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 메소드에 권한별 접근 허용을 하고싶을 때
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 이 메소드가 실행되기 직전에 실행됨, 여러개의 권한을 걸고싶을때
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }

}
