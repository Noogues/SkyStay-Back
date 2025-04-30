package com.example.skystayback.dtos.airplanes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAirplanesTypesVO {
    private String name;
    private String manufacturer;
    private Integer capacity;
}
