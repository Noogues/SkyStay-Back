package com.example.skystayback.dtos.airline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirlineTableVO {
    private String code;
    private String name;
    private String image;
    private String phone;
    private String email;
    private String website;
    private String iataCode;
}
