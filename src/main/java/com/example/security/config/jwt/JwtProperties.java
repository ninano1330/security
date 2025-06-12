package com.example.security.config.jwt;

public interface JwtProperties {
    String SECRET = "jeon";
    int EXPIRATION_TIME = 60000 * 10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
