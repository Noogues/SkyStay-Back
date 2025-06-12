package com.example.skystayback.dtos.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneVO {
    private String code;
    private String model;
    private String registrationNumber;
    private Integer yearOfManufacture;
    private Integer capacity;
    private String manufacturer;
    private String airplaneTypeName;
}
