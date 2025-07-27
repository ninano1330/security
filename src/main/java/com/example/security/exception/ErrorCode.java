package com.example.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS("USER_EXISTS", "이미 존재하는 회원입니다.");
    private final String code;
    private final String message;
}
