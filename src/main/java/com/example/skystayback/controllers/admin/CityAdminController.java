package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.city.CityAddVO;
import com.example.skystayback.dtos.city.CityTableVO;
import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.dtos.city.CountryAddVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
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
    public ResponsePaginatedVO<CityVO> getCities(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return cityService.getCities(pageVO);
    }

    @GetMapping("/all/table")
    public ResponsePaginatedVO<CityTableVO> getCitiesTable(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return cityService.getCitiesTable(pageVO);
    }

    @PostMapping("/add")
    public ResponseVO<Void> addCity(@RequestBody List<CityAddVO> cityVO) {
        return  cityService.addCityCountry(cityVO);
    }

    @PostMapping("/add/country")
    public ResponseVO<Void> addCountry(@RequestBody CountryAddVO country) {
        return  cityService.addCountry(country);
    }

}
