package com.example.security.Service;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.oauth.GoogleUserInfo;
import com.example.security.config.oauth.OAuth2UserInfo;
import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registrationId로 어떤 OAuth로 로그인했는지 확인
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String tokenValue = accessToken.getTokenValue();
        OAuth2UserInfo oAuth2UserInfo = null;

        if("google".equals(provider)) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId; // google_1234
        String password = bCryptPasswordEncoder.encode("password");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

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

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
