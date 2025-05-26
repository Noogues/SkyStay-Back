package com.example.skystayback.dtos.flights;

import com.example.skystayback.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightsTableVO {
    private String code;
    private LocalTime departureTime;
    private FlightStatus status;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeArrival;

    private AirlineTableVO airline;
    private AirportTableVO departureAirport;
    private AirportTableVO arrivalAirport;
    private AirplaneTableVO airplane;
}
