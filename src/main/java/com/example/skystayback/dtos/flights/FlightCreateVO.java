package com.example.skystayback.dtos.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlightCreateVO {
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeArrival;
    private Long airlineId;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private Long airplaneId;
}
