package com.example.skystayback.dtos.airplanes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAirplaneStatusVO {
    private String airplaneCode;
    private String status;
}
