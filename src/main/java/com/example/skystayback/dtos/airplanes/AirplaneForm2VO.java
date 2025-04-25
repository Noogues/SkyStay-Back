package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.SeatClass;
import com.example.skystayback.enums.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirplaneForm2VO {

    // Seat configuration:
    private Integer id;
    private SeatClass seatClass;
    private String seatPattern;
    private Integer totalRows;
    private String description;


    // Airplane cabin (necesita airplane_id y seat_configuration_id):
    private Integer Integer;
    private Integer rowStart;
    private Integer rowEnd;
}
