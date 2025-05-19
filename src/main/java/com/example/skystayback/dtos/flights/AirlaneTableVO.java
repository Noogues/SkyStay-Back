package com.example.skystayback.dtos.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlaneTableVO {
    private String code;
    private String name;
    private String iataCode;
}
