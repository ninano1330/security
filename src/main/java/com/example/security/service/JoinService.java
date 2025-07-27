package com.example.security.service;

import com.example.security.dto.UserDto;
import com.example.security.entity.User;
import com.example.security.exception.CustomException;
import com.example.security.exception.ErrorCode;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public String join(UserDto userDto) throws Exception {
        String email = userDto.getEmail();

        User findUser = userRepository.findByEmail(email);
        if (findUser != null) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.setRole("ROLE_USER");

        User user = userDto.toEntity();
        userRepository.save(user); // Uncomment this line to save the user to the database

        return "1";
    }
}
