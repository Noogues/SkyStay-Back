package com.example.skystayback.controllers;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.services.UserService;
import com.example.skystayback.services.email.EmailService;
import com.example.skystayback.services.email.EmailTemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
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

    @PostMapping("/verify-code")
    public ResponseVO<MessageResponseVO> verifyCode(@RequestBody Map<String, Object> request) {
        Integer code = Integer.parseInt(request.get("code").toString());
        String email = (String) request.get("email");
        return userService.code_check(code, email);
    }

    @PostMapping("/resend-code")
    public ResponseVO<MessageResponseVO> resendCode(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        return userService.resendCode(email);
    }

    @PostMapping("/send-code-password")
    public ResponseVO<MessageResponseVO> sendPasswordCode(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        return userService.sendPasswordCode(email);
    }

    @PostMapping("/change-password")
    public ResponseVO<MessageResponseVO> changePassword(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        return userService.resetPassword(email, password);
    }

    @PostMapping("/send-test-email")
    public String sendTestEmail() {
        try {
            // Configura los datos del correo
            String to = "rjaencobos@safareyes.es";
            String subject = "Correo de prueba";
            EmailTemplateType templateType = EmailTemplateType.REGISTRATION;

            // Variables para la plantilla
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", "Usuario de prueba");
            variables.put("message", "Este es un correo de prueba enviado desde el sistema.");

            // Llama al servicio de correo
            emailService.sendEmail(to, subject, templateType, variables);
            return "Correo enviado exitosamente.";
        } catch (Exception e) {
            return "Error al enviar el correo: " + e.getMessage();
        }
    }
}