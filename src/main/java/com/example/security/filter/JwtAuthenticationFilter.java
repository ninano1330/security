package com.example.security.filter;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.dto.UserDto;
import com.example.security.entity.User;
import com.example.security.util.JWTTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("successfulAuthentication :: 인증 완료");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        JWTTokenUtil jWTTokenUtil = new JWTTokenUtil();

        /** response header Authorization */
        //        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        /** response Set-Cookie */
        String jwtToken = jWTTokenUtil
                .createJwtToken(principalDetails.getUser().getId(), principalDetails.getUser().getUsername(), principalDetails.getUser().getRole());
        String cookieValue = jWTTokenUtil.createCookieValue(jwtToken);
        response.addHeader("Set-Cookie", cookieValue);

//        super.successfulAuthentication(request, response, chain, authentication);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication :: 로그인 시도");

        try {
            ObjectMapper om = new ObjectMapper();
//            User user = om.readValue(request.getInputStream(), User.class);
            UserDto user = om.readValue(request.getInputStream(), UserDto.class);

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService.loadUserByUsername() 실행
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
