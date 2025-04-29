package com.example.skystayback.exceptions;

import com.example.skystayback.dtos.common.ErrorResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseVO> handleApiException(ApiException ex) {
        ErrorResponseVO errorResponse = ErrorResponseVO.builder()
                .title(ex.getTitle())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseVO> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponseVO errorResponse = ErrorResponseVO.builder()
                .title("Usuario no encontrado")
                .message(ex.getMessage())
                .errorCode("USER_NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}