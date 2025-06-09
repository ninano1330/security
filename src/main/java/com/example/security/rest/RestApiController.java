package com.example.security.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
    @GetMapping("home")
    public String home() {
        return "Welcome to the home page!";
    }
}
