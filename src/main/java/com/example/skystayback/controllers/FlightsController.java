package com.example.skystayback.controllers;

import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.flights.FlightClientVO;
import com.example.skystayback.services.FlightsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/flights")
public class FlightsController {

    private final FlightsService flightsService;

    @GetMapping("/all")
    public ResponsePaginatedVO<FlightClientVO> getAllFlights(
            @RequestParam(defaultValue = "30") Integer limit,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) Float price) {
        PageVO pageVO = new PageVO(limit, page);
        return flightsService.getAllFlights(pageVO, origin, destination, airline, price);
    }
}
