package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import com.example.security.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email);
        int id = user.getId();
        String username = user.getUsername();

        TokenUtil tokenUtil = new TokenUtil();

        String jwtToken = tokenUtil.createJwtToken(id, username);
        String cookieValue = tokenUtil.createCookieValue(jwtToken);

        response.addHeader("Set-Cookie", cookieValue);

        // 4. 홈 등으로 리디렉션
        response.sendRedirect("/");
    }
}
