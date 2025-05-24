package com.example.security.config;


import com.example.security.Service.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // 서비스 레이어 보안 기능 - secrued, prePostEnabled 어노테이션 활성화
public class SecurityConfiguration {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
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
                .csrf((crsf) -> crsf.disable())
        ;

        return httpSecurity.build();
    }
}
