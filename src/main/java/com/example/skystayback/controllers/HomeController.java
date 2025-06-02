package com.example.skystayback.controllers;

import com.example.skystayback.dtos.city.CityImageVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {


    private final HomeService homeService;

    @GetMapping("/list-five-citys")
    public ResponseVO<List<CityImageVO>> listFiveCitys() {
        return homeService.listFiveCitys();
    }
}
