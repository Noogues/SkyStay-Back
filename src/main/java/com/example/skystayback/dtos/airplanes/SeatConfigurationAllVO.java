package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatConfigurationAllVO {
    private String seatPattern;
    private String description;

}
