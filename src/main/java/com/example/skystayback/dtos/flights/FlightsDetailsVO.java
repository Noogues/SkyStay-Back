package com.example.skystayback.dtos.flights;

import com.example.skystayback.dtos.airline.AirlineTableVO;
import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightsDetailsVO {
    private String code;
    private LocalTime departureTime;
    private FlightStatus status;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeArrival;

    private AirlineTableVO airline;
    private AirportAdminVO departureAirport;
    private AirportAdminVO arrivalAirport;
    private AirplaneShowVO airplane;
}
