package com.example.springsecuritypractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인에 등록됨.
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/user/**").authenticated() // 로그인 한 사람만 접근 가능
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // admin 또는 manager
                                .requestMatchers("/admin/**").hasRole("ADMIN") // admin만
                                .anyRequest().permitAll() // 나머지 url은 모든 접근 허용
                ).formLogin((formLogin) ->
                        formLogin.loginPage("/login") // formLogin 활성화, 권한이 없는 경우 403 페이지가 아닌 login 페이지로 이동
                );

        return http.build();
    }


}
