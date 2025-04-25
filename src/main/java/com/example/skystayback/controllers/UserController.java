package com.example.skystayback.controllers;

import com.example.skystayback.dtos.common.AuthenticationVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.common.UserLoginVO;
import com.example.skystayback.dtos.common.UserRegisterVO;
import com.example.skystayback.dtos.common.TokenDecodeVO;
import com.example.skystayback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseVO<AuthenticationVO> register(@RequestBody UserRegisterVO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public ResponseVO<AuthenticationVO> login(@RequestBody UserLoginVO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/decode-token")
    public ResponseVO<TokenDecodeVO> decodeToken(@RequestHeader("Authorization") String token) {
        return userService.decodeToken(token);
    }
}