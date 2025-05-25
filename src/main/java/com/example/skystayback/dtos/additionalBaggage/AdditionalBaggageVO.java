package com.example.skystayback.dtos.additionalBaggage;

import com.example.skystayback.dtos.airline.AirlineTableVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalBaggageVO {
    private String name;
    private Float weight;
    private Float extraAmount;
    private Long airline_id;
}
