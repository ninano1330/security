package com.example.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.jwt.JwtProperties;
import com.example.security.dto.UserDto;
import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// 시큐리티가 filter를 가지고 있는데 그 필터 중에 BasicAuthenticationFilter가 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
// 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안 탐
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter  {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        log.info("doFilterInternal :: 권한이나 인증이 필요함");

        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

        Cookie[] cookies = request.getCookies();
        String jwtToken = null;

        /** JWT 토큰을 헤더에서 추출 */
//        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
//
//        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
//            chain.doFilter(request, response);
//            return;
//        }

//        String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        /** JWT 토큰을 쿠키에서 추출 */
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
            }
        }

        if(jwtToken == null) {
            chain.doFilter(request, response);
            return;
        }

        // 서명
        String username
                = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();

        int id
                = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("id").asInt();

        String role
                = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("role").asString();

        if(username != null) {
//            User user = userRepository.findByUsername(username);
            UserDto user = new UserDto();
            user.setId(id);
            user.setUsername(username);
            user.setRole(role);

            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
