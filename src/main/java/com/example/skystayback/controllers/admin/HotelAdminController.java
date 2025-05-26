package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.common.AddImageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.hotel.*;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.enums.RoomType;
import com.example.skystayback.models.RoomConfiguration;
import com.example.skystayback.services.admin.HotelAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("admin/hotels")
public class HotelAdminController {

    private final HotelAdministrationService hotelService;

    @PostMapping("/add")
    public ResponseVO<String> addHotel(@RequestBody HotelFormVO hotelAddVO) {
        return hotelService.addHotel(hotelAddVO);
    }

    @GetMapping("/all")
    public ResponsePaginatedVO<HotelAdminVO> getAllHotels(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return hotelService.getAllHotels(pageVO);
    }

    @PostMapping("/add/image")
    public ResponseVO<Void> addHotelImage(@RequestBody AddImageVO form) {
        return hotelService.addHotelImage(form);
    }

    @GetMapping("/rooms/type/all")
    public ResponseVO<List<RoomType>> getAllRoomTypes() {
        return hotelService.getAllRoomTypes();
    }

    @GetMapping("/rooms-configuration/all")
    public ResponseVO<List<RoomConfigurationVO>> getAllRoomConfigurations() {
        return hotelService.getAllRoomConfigurations();
    }

    @GetMapping("/{hotelCode}")
    public ResponseVO<ShowHotelDetails> getHotelByCode(@PathVariable String hotelCode) {
        return hotelService.getHotel(hotelCode);
    }

    @PostMapping("/add-image/room")
    public ResponseVO<Void> addRoomImage(@RequestBody AddRoomImageVO form) {
        return hotelService.addRoomImage(form);
    }

    @PostMapping("/update")
    public ResponseVO<Void> editHotel(@RequestBody EditHotelVO hotelEditVO) {
        return hotelService.editHotel(hotelEditVO);
    }
}
