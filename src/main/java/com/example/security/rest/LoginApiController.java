package com.example.security.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginApiController {
    @PostMapping("login")
    public String login() {
        log.info("로그인 요청");
        return "로그인 성공";
    }
}
