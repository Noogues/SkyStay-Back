package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.apartments.AddRoomImageVO;
import com.example.skystayback.dtos.common.AddImageVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.hotel.*;
import com.example.skystayback.services.admin.AparmentAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/apartments")
public class ApartmentAdminController {

    private final AparmentAdministrationService aparmentAdministrationService;

    @PostMapping("/add")
    public ResponseVO<String> addApartment(@RequestBody HotelFormVO hotelAddVO) {
        return aparmentAdministrationService.addApartment(hotelAddVO);
    }

    @PostMapping("/add/image")
    public ResponseVO<Void> addApartmentImage(@RequestBody AddImageVO form) {
        return aparmentAdministrationService.addApartmentImage(form);
    }

    @GetMapping("/all")
    public ResponsePaginatedVO<HotelAdminVO> getAllApartment(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        page = Math.max(page, 1);
        PageVO pageVO = new PageVO(limit, page);
        return aparmentAdministrationService.getAllApartments(pageVO);
    }

    @GetMapping("/{apartmentCode}")
    public ResponseVO<ShowHotelDetails> getApartmentByCode(@PathVariable String apartmentCode) {
        return aparmentAdministrationService.getApartmentByCode(apartmentCode);
    }

    @PostMapping("/add-image/room")
    public ResponseVO<Void> addRoomImage(@RequestBody AddRoomImageVO form) {
        return aparmentAdministrationService.addRoomImage(form);
    }

    @PostMapping("/update")
    public ResponseVO<Void> editApartment(@RequestBody EditHotelVO apartment) {
        return aparmentAdministrationService.editApartment(apartment);
    }
}
