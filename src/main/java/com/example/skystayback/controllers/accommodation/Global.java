package com.example.skystayback.controllers.accommodation;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.common.AccommodationResponseVO;
import com.example.skystayback.services.accommodation.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accommodations")
public class Global {

    @Autowired
    private GlobalService globalService;

    @GetMapping("")
    public ResponseVO<List<AccommodationResponseVO>> getAccommodations(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDate checkIn,
            @RequestParam(required = false) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "1") Integer adults,
            @RequestParam(required = false, defaultValue = "0") Integer children,
            @RequestParam(required = false, defaultValue = "1") Integer rooms) {

        return globalService.searchAccommodations(destination, checkIn, checkOut, adults, children, rooms);
    }

    @GetMapping("/cities")
    public List<String> getCities() {
        return globalService.getAllCities();
    }

    @GetMapping("/destinations")
    public List<DestinationVO> getDestinations() {
        return globalService.getTopDestinations();
    }
}