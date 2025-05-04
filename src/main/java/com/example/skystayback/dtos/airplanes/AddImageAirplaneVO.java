package com.example.skystayback.dtos.airplanes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddImageAirplaneVO {
    private String airplaneCode;
    private String image;
}
