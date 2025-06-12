package com.example.skystayback.dtos.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightClientVO {
    private String code;
    private String airlineName;
    private String departureCity;
    private String departureAirport;
    private String departureIATA;
    private String arrivalCity;
    private String arrivalAirport;
    private String arrivalIATA;
    private LocalDateTime departureTime;
    private LocalDateTime dateTimeArrival;
    private Float price;
    private Long seatsLeft;
}
