package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.hotel.HotelApartmentsAddVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.ApartmentAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("admin/apartment")
public class AparmentAdminController {

    private final ApartmentAdministrationService apartmentService;

    @PostMapping("/apartment/add")
    public ResponseVO<String> addApartment(@RequestBody HotelApartmentsAddVO apartmentAddVO) {
        return apartmentService.addApartment(apartmentAddVO);
    }
}
