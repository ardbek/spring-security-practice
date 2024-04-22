package com.example.springsecuritypractice.config.auth;

import com.example.springsecuritypractice.model.User;
import com.example.springsecuritypractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// PrincipalDetails 객체를 Authentication 객체로 변환하는 Service
// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 실행됨.(규칙임.)
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session = Authentication = UserDetails
    // return 하게 되면 Authentication(내부 UserDetails) 에 들어가고
    // 시큐리티 session(내부 Authentication(내부 UserDetails)) 에 들어가게됨. => 로그인 완료
    @Override //파라미터는 DTO에 있는 컬럼명을 그대로 써줘야됨.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
