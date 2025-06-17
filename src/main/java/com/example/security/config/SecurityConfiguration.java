package com.example.security.config;


import com.example.security.filter.JwtAuthenticationFilter;
import com.example.security.filter.JwtAuthorizationFilter;
import com.example.security.repository.UserRepository;
import com.example.security.service.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // 서비스 레이어 보안 기능 - secrued, prePostEnabled 어노테이션 활성화
public class SecurityConfiguration {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        httpSecurity
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용할 때는 STATELESS로 설정
//                )
                .addFilter(corsConfig.corsFilter()) // CORS 설정 필터 추가
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf((crsf) -> crsf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                        .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin((formLogin ->
                        formLogin.loginPage("/loginForm")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/"))
                )
                .oauth2Login(oath2Login ->
                        oath2Login.loginPage("/loginForm")
                                .userInfoEndpoint(userInfoEndPoint -> userInfoEndPoint.userService(principalOauth2UserService))
                                .defaultSuccessUrl("/")
                )
        ;

        return httpSecurity.build();
    }
}
