package com.example.security.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexApiController {
    @GetMapping("home")
    public String home() {
        return "Welcome to the home page!";
    }

    @GetMapping("token")
    public String token() {
        return "Welcome to the home page!";
    }
}
