package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.common.UserInfoVO;
import com.example.skystayback.dtos.user.*;
import com.example.skystayback.services.admin.UserAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/users")
public class UserAdminController {

    private final UserAdministrationService userService;

    @GetMapping("/getInfo/{userCode}")
    public ResponseVO<UserInfoVO> getUserInfoByCode(@PathVariable String userCode) {
        return userService.getUserInfoByCode(userCode);
    }

    @GetMapping("/all")
    public ResponsePaginatedVO<UserAdminVO> getUsers(
            @RequestParam(defaultValue = "30") Integer limit,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String search) {
        PageVO pageVO = new PageVO(limit, page);
        return userService.getUsers(pageVO, search);
    }

    @GetMapping("/{userCode}/airline-ratings")
    public ResponsePaginatedVO<AirlineRatingVO> getAirlaneRating(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return userService.getAirlaneRating(userCode, pageVO);
    }

    @GetMapping("/{userCode}/apartment-ratings")
    public ResponsePaginatedVO<ApartmentRatingVO> getApartmentRating(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return userService.getApartmentRating(userCode, pageVO);
    }

    @GetMapping("/{userCode}/hotel-ratings")
    public ResponsePaginatedVO<HotelRatingVO> getHotelRating(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return userService.getHotelRating(userCode, pageVO);
    }

    @GetMapping("/{userCode}/order-apartments")
    public ResponsePaginatedVO<OrderApartmentVO> getOrderApartment(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return userService.getOrderApartment(userCode, pageVO);
    }

//    @GetMapping("/{userCode}/order-flights")
//    public ResponsePaginatedVO<OrderFlightVO> getOrderFlight(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
//
//        PageVO pageVO = new PageVO(limit, page);
//        return userService.getOrderFlight(userCode, pageVO);
//    }

    @GetMapping("/{userCode}/order-hotels")
    public ResponsePaginatedVO<OrderHotelVO> getOrderHotel(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return userService.getOrderHotel(userCode, pageVO);
    }
}
