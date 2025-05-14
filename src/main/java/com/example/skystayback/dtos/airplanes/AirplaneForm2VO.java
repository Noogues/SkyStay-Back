package com.example.skystayback.dtos.airplanes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirplaneForm2VO {

    // Airplane
    private Long airplane_id;

    // Seat configuration:
    private Long seat_configuration_id;

    // Airplane cabin (necesita airplane_id y seat_configuration_id):
    private String seat_class;
    private Integer rowStart;
    private Integer rowEnd;
}
