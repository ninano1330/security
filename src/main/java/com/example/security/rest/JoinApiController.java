package com.example.security.rest;

import com.example.security.dto.UserDto;
import com.example.security.entity.User;
import com.example.security.exception.CustomException;
import com.example.security.repository.UserRepository;
import com.example.security.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinApiController {
    @Autowired
    private JoinService joinService;

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity join(@RequestBody UserDto userDto) {
        try {
            String join = joinService.join(userDto);
        } catch (CustomException e){
            return new ResponseEntity<>(e.getErrorCode().getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
