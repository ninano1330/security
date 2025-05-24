package com.example.security.rest;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(
            Authentication authentication
            , @AuthenticationPrincipal PrincipalDetails userDetails) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        System.out.println("userDetails = " + userDetails.getUser());

        return "세션 정보 확인";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(
            Authentication authentication
            , @AuthenticationPrincipal OAuth2User oauth) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        return "세션 정보 확인";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails.getUser()); = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @GetMapping("/info")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public String info() {
        return "개인정보";
    }

    @GetMapping("/data")
    @ResponseBody
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String data() {
        return "데이터정보";
    }
}
