package com.example.springsecuritypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인에 등록됨.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // @EnableGlobalMethodSecurity 는 deprecated 됨 -> secure 어노테이션 활성화 ()
// securedEnabled -> 메소드에 @Secured 어노테이션 활성화
// prePostEnabled -> preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

    // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/user/**").authenticated() // 로그인 한 사람만 접근 가능, 인증만 되면 들어갈 수 있는 주소
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // admin 또는 manager
                                .requestMatchers("/admin/**").hasRole("ADMIN") // admin만
                                .anyRequest().permitAll() // 나머지 url은 모든 접근 허용
                ).formLogin((formLogin) ->
                        formLogin.loginPage("/loginForm") // formLogin 활성화, 권한이 없는 경우 403 페이지가 아닌 login 페이지로 이동
                                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌.
                                .defaultSuccessUrl("/")// 로그인 성공시 url 만약 다른 Url 이 있으면 자동으로 거기로 보내주고, 없으면 defauilt url로 이동
//                                .usernameParameter("username2") principalDetailsService와 Model의 필드명이 다를 때 usernameParameter로 매핑시켜준다.
                );

        return http.build();
    }


}
