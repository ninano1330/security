package com.example.security.service;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.oauth.FacebookUserInfo;
import com.example.security.config.oauth.GoogleUserInfo;
import com.example.security.config.oauth.NaverUserInfo;
import com.example.security.config.oauth.OAuth2UserInfo;
import com.example.security.dto.UserDto;
import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser :: OAuth2 로그인 요청");
        // registrationId로 어떤 OAuth로 로그인했는지 확인
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String tokenValue = accessToken.getTokenValue();
        OAuth2UserInfo oAuth2UserInfo = null;

        if("google".equals(provider)) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if("facebook".equals(provider)) {
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if("naver".equals(provider)){
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId; // google_1234
        String password = bCryptPasswordEncoder.encode("password");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_MANAGER";

        User user = userRepository.findByUsername(username);

        if(user == null){
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(user);
        }

        UserDto userDto = user.toDto();

        return new PrincipalDetails(userDto, oAuth2User.getAttributes());
    }
}
