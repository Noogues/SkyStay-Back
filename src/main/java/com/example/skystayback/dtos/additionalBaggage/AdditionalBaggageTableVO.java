package com.example.skystayback.dtos.additionalBaggage;

import com.example.skystayback.dtos.airline.AirlineTableVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalBaggageTableVO {
    private Long id;
    private String name;
    private Float weight;
    private Float extraAmount;
    private AirlineTableVO airline;
}
