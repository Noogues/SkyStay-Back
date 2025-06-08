package com.example.skystayback.dtos.flights;

import com.example.skystayback.dtos.meal.MealTableVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<CabinsPriceVO> cabins;

    private List<MealVO> meals;
}
