package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.hotel.HotelApartmentsAddVO;
import com.example.skystayback.dtos.hotel.RoomAdminVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.HotelAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("admin/hotels")
public class HotelAdminController {

    private final HotelAdministrationService hotelService;

    @GetMapping("/{hotelCode}/rooms")
    public ResponseVO<List<RoomAdminVO>> getRoomsByHotelCode(@PathVariable String hotelCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return hotelService.getRoomsByHotelCode(hotelCode, pageVO);
    }

    @PostMapping("/add")
    public ResponseVO<String> addHotel(@RequestBody HotelApartmentsAddVO hotelAddVO) {
        return hotelService.addHotel(hotelAddVO);
    }
}
