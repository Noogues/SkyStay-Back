package com.example.skystayback.controllers;

import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.flights.*;
import com.example.skystayback.dtos.meal.MealVO;
import com.example.skystayback.services.FlightsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/flights")
public class FlightsController {

    private final FlightsService flightsService;

    @GetMapping("/all")
    public ResponsePaginatedVO<FlightClientVO> getAllFlights(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page, @RequestParam(required = false) String origin, @RequestParam(required = false) String destination, @RequestParam(required = false) String airline, @RequestParam(required = false) Float price) {
        PageVO pageVO = new PageVO(limit, page);
        return flightsService.getAllFlights(pageVO, origin, destination, airline, price);
    }

    @GetMapping("/details/{flightCode}")
    public ResponseVO<AllDetailsFlightsVO> getFlightDetails(@PathVariable String flightCode) {
        return flightsService.getFlightDetails(flightCode);
    }

    @GetMapping("/cabins/{flightCode}")
    public ResponseVO<List<CabinFlightDetailsVO>> getCabinsByCode(@PathVariable String flightCode) {
        return flightsService.getCabinsForFlightByCode(flightCode);
    }

    @GetMapping("/meals/{flightCode}")
    public ResponseVO<List<MealVO>> getMealsByFlightCode(@PathVariable String flightCode) {
        return flightsService.getMealsByFlightCode(flightCode);
    }

    @PostMapping("/purchase/{flightCode}")
    public ResponseVO<Void> purchaseTicketsFlight(@PathVariable String flightCode, @RequestBody List<FlightPurchaseVO> flightPurchaseDTO, @RequestHeader("Authorization") String token,  @RequestParam(required = false) String language) {
        return flightsService.purchaseTicketsFlight(flightCode, flightPurchaseDTO, token, language);
    }
}
