package com.example.springsecuritypractice.controller;

import com.example.springsecuritypractice.model.User;
import com.example.springsecuritypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @GetMapping("/user")
    public @ResponseBody String user() {
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
