package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.airline.*;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.AirlineAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/airline")
public class AirlineAdminController {

    private final AirlineAdministrationService airlineService;

    @GetMapping("/all")
    public ResponsePaginatedVO<AirlineTableVO> getAllAirlines(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return airlineService.getAllAirlines(pageVO);
    }

    @GetMapping("/all/reduced")
    public ResponsePaginatedVO<AirlineReducedVO> getAllAirlinesReduced(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return airlineService.getAllAirlinesReduced(pageVO);
    }

    @PostMapping("/add")
    public ResponseVO<String> addAirline(@RequestBody AirlineAddVO airlineAddVO) {
        return airlineService.addAirline(airlineAddVO);
    }

    @PostMapping("/add/image")
    public ResponseVO<Void> addAirlineImage(@RequestBody AirlineAddImageVO airlineAddVO) {
        return airlineService.addImageToAirline(airlineAddVO);
    }

    @PostMapping("/edit")
    public ResponseVO<Void> editAirline(@RequestBody AirlineEditVO airlineAddVO) {
        return airlineService.editAirline(airlineAddVO);
    }
}
