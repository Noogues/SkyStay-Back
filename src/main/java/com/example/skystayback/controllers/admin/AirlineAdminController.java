package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.airline.AirlineAddVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.AirlineAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("admin/airline")
public class AirlineAdminController {

    private final AirlineAdministrationService airlineService;

    @PostMapping("/add")
    public ResponseVO<String> addAirline(@RequestBody AirlineAddVO airlineAddVO) {
        return airlineService.addAirline(airlineAddVO);
    }
}
