package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.dtos.airports.AirportFormVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.flights.FlightsTableVO;
import com.example.skystayback.services.admin.AirportsAdministrationService;
import com.example.skystayback.services.admin.FlightsAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/flights")
public class FlightsAdminController {

    private final FlightsAdministrationService flightsService;


    @GetMapping("/all")
    public ResponsePaginatedVO<FlightsTableVO> getFlights(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return flightsService.getAllFlights(pageVO);
    }


}
