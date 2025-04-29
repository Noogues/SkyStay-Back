package com.example.skystayback.dtos.airports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportFormVO {
    private String name;
    private String iataCode;
    private String description;
    private String terminal;
    private String gate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String timezone;
    private String city;
}
