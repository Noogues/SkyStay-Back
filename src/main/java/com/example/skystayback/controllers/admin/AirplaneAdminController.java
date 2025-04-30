package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.airplanes.*;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.SeatClass;
import com.example.skystayback.enums.Status;
import com.example.skystayback.services.admin.AirplaneAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("admin/airplanes")
public class AirplaneAdminController {

    @Autowired
    private final AirplaneAdministrationService airplaneAdminService;

    @GetMapping("/all")
    public ResponsePaginatedVO<AirplaneShowVO> getAllAirplanes(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return airplaneAdminService.getAllAirplanes(pageVO);
    }

    @GetMapping("/types/all")
    public ResponseVO<List<AirplaneTypeEnum>> getAllAirplaneTypeEnum() {
        return airplaneAdminService.getAllAirplaneTypeEnum();
    }

    @GetMapping("/status/all")
    public ResponseVO<List<Status>> getAllStatusEnum() {
        return airplaneAdminService.getAllStatusEnum();
    }

    @GetMapping("/seat-classes/all")
    public ResponseVO<List<SeatClass>> getAllSeatClassEnum() {
        return airplaneAdminService.getAllSeatClassEnum();
    }

    @GetMapping("/seat-configurations/all")
    public ResponseVO<List<SeatConfigurationVO>> getAllSeatConfigurations() {
        return airplaneAdminService.getAllSeatConfiguration();
    }

    @PostMapping("/seat-configuration/create")
    public ResponseVO<Void> createSeatConfiguration(@RequestBody CreateSeatConfigurationVO seatConfiguration) {
        return airplaneAdminService.createSeatConfiguration(seatConfiguration);
    }

    @GetMapping("/airplanes-types/all")
    public ResponseVO<List<AirplanesTypesVO>> getAllAirplanesTypes() {
        return airplaneAdminService.getAllAirplanesTypes();
    }

    @PostMapping("/airplanes-types/create")
    public ResponseVO<Void> createAirplanesTypes(@RequestBody CreateAirplanesTypesVO airplanesTypes) {
        return airplaneAdminService.createAirplanesTypes(airplanesTypes);
    }

    @PostMapping("/create/part1")
    public ResponseVO<Long> createAirplane(@RequestBody AirplaneForm1VO form) {
        return airplaneAdminService.createAirplanePart1(form);
    }

    @PostMapping("/create/part2")
    public ResponseVO<Void> createAirplanePart2(@RequestBody List<AirplaneForm2VO> form) {
        return airplaneAdminService.createAirplanePart2(form);
    }



}
