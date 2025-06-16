package com.example.skystayback.dtos.profile;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiMessage {
    private String message;
    private int code;
    private String timestamp;

    public ApiMessage(String message, int code) {
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now().toString();
    }
}