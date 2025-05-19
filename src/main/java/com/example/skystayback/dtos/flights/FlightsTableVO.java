package com.example.skystayback.dtos.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightsTableVO {
    private String code;
    private LocalDateTime depatureTime;
    private String status;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeArrival;

    private AirlaneTableVO airlane;
    private AirportTableVO depatureAirport;
    private AirportTableVO arrivalAirport;
    private AirplaneTableVO airplane;
}
