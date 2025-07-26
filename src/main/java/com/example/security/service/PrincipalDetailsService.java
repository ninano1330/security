package com.example.security.service;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.dto.UserDto;
import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername :: 로그인");
        User user = userRepository.findByUsername(username);
        UserDto userDto = user.toDto();

        if(user != null) return new PrincipalDetails(userDto);
        return null;
    }
}
