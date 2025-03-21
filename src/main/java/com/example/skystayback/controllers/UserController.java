package com.example.skystayback.controllers;

import com.example.skystayback.dtos.AuthenticationDTO;
import com.example.skystayback.dtos.UserLoginDTO;
import com.example.skystayback.dtos.UserRegisterDTO;
import com.example.skystayback.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDTO> register(@RequestBody UserRegisterDTO userDTO) {
        AuthenticationDTO authDTO = userService.register(userDTO);
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody UserLoginDTO userDTO) {
        AuthenticationDTO authDTO = userService.login(userDTO);
        return ResponseEntity.ok(authDTO);
    }
}