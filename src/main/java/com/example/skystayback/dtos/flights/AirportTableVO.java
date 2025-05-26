package com.example.skystayback.dtos.flights;

import com.example.skystayback.dtos.city.CityVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportTableVO {
    private String code;
    private String name;
    private String iataCode;
    private CityVO city;
}
