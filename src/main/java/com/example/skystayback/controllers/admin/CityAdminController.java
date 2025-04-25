package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.city.CityAddVO;
import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.CityAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("admin/city")
public class CityAdminController {

    private final CityAdministrationService cityService;

    @GetMapping("/all")
    public ResponseVO<List<CityVO>> getCities() {
        return cityService.getCities();
    }

    @PostMapping("/add")
    public ResponseVO<String> addCity(@RequestBody List<CityAddVO> cityVO) {
        return  cityService.addCityCountry(cityVO);
    }

}
