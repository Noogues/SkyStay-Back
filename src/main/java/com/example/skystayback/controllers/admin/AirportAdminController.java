package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.dtos.airports.AirportFormVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.AirportsAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/airports")
public class AirportAdminController {

    private final AirportsAdministrationService airportsService;


    @GetMapping("/all")
    public ResponsePaginatedVO<AirportAdminVO> getAirports(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return airportsService.getAirports(pageVO);
    }

    @PostMapping("/create")
    public ResponseVO<String> createAirport(@RequestBody AirportFormVO airportAdminVO) {
        return airportsService.createAirport(airportAdminVO);
    }

    @PutMapping("/update/{code}")
    public ResponseVO<String> updateAirport(@PathVariable String code, @RequestBody AirportFormVO airportAdminVO) {
        return airportsService.updateAirport(code, airportAdminVO);
    }
}
