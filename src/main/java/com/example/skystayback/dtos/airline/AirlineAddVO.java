package com.example.skystayback.dtos.airline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineAddVO {
    private String name;
    private String phone;
    private String email;
    private String website;
    private String iataCode;
}
