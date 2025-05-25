package com.example.skystayback.dtos.additionalBaggage;

import com.example.skystayback.dtos.airline.AirlineTableVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalBaggageReducedVO {
    private Long id;
    private String name;
}
