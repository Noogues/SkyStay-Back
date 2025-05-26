package com.example.skystayback.dtos.airline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlineAddImageVO {
    private String code;
    private String url;
}
