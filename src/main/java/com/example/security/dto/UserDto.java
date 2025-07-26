package com.example.security.dto;

import com.example.security.entity.User;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
public class UserDto {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String provider;
    private String providerId;
    private Timestamp createDate;

    public User toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(this, User.class);

        return user;
    }
}
