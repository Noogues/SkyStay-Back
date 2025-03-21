package com.example.skystayback.exceptions;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String title;
    private final String errorCode;

    public ApiException(String title, String message, String errorCode) {
        super(message);
        this.title = title;
        this.errorCode = errorCode;
    }
}