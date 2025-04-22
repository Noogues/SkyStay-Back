package com.example.skystayback.dtos.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseVO {
    private String message;
    private Integer code;
    private LocalDateTime timestamp;
}
