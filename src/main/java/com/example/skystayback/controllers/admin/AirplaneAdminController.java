package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.airplanes.AirplaneShowVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import com.example.skystayback.services.admin.AirplaneAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
