package com.example.skystayback.dtos.airports;

import com.example.skystayback.dtos.city.CityVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportAdminVO {
    private String code;
    private String name;
    private String iataCode;
    private String description;
    private String terminal;
    private String gate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String timezone;
    private CityVO city;
}
