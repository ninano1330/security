package com.example.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.jwt.JwtProperties;

import java.util.Date;

public class JWTTokenUtil {
    public String createJwtToken(int id, String username, String role) {
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim("id", id)
                .withClaim("username", username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return jwtToken;
    }

    public String createCookieValue(String jwtToken) {
        return JwtProperties.TOKEN_NAME + "=" + jwtToken + ";"
                + " Path=/; HttpOnly; Max-Age=" + JwtProperties.EXPIRATION_TIME / 1000;
    }
}
