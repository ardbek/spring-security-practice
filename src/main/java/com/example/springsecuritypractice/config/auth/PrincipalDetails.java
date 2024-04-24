package com.example.springsecuritypractice.config.auth;
// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어줌. (Security ContextHolder라는 키값으로 담아서 세션 정보를 저장)
// Session에 들어갈 수 있는 오브젝트는 Authentication 타입의 객체만 들어갈 수 있음.
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트의 타입도 정해져 있음 => UserDetails 타입 객체

// Security Session 영역 => Authentication 타입의 객체만 들어감 => 사용자 정보는 UserDetails(PrincipalDetails) 타입으로 저장되어 있음.

import com.example.springsecuritypractice.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤포지션
    private Map<String,Object> attributes; //OAuth2 로그인 정보

    // 일반 로그인 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인 생성자
    public PrincipalDetails(User user, Map<String,Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }



    // 해당 User의 권한을 return
    // 반환 타입이 Collection<GrantedAuthority> 이기 때문에 String 타입으로 가지고 있는 Role을 변환시켜준다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 사용자의 계정이 만료되었는지 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 사용자 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 1년동안 회원이 로그인을 안하면 휴면 계정으로 전환할 때
        // 로그인 날짜 컬럼과 비교해서 return false;

        return true;
    }


    // OAuth2User ==================================================
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
