package com.example.skystayback.controllers;

import com.example.skystayback.dtos.admin.add.AirlineAddVO;
import com.example.skystayback.dtos.admin.add.AirportFormVO;
import com.example.skystayback.dtos.admin.add.CityAddVO;
import com.example.skystayback.dtos.admin.add.HotelApartmentsAddVO;
import com.example.skystayback.dtos.admin.all.*;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.common.UserInfoVO;
import com.example.skystayback.services.AdminService;
import com.example.skystayback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdministrationController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdministrationController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public ResponsePaginatedVO<UserAdminVO> getUsers(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return adminService.getUsers(pageVO);
    }

    @GetMapping("/users/{userCode}/airline-ratings")
    public ResponsePaginatedVO<AirlineRatingVO> getAirlaneRating(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return adminService.getAirlaneRating(userCode, pageVO);
    }

    @GetMapping("/users/{userCode}/apartment-ratings")
    public ResponsePaginatedVO<ApartmentRatingVO> getApartmentRating(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return adminService.getApartmentRating(userCode, pageVO);
    }

    @GetMapping("/users/{userCode}/hotel-ratings")
    public ResponsePaginatedVO<HotelRatingVO> getHotelRating(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return adminService.getHotelRating(userCode, pageVO);
    }

    @GetMapping("/users/{userCode}/order-apartments")
    public ResponsePaginatedVO<OrderApartmentVO> getOrderApartment(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return adminService.getOrderApartment(userCode, pageVO);
    }

    @GetMapping("/users/{userCode}/order-flights")
    public ResponsePaginatedVO<OrderFlightVO> getOrderFlight(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return adminService.getOrderFlight(userCode, pageVO);
    }

    @GetMapping("/users/{userCode}/order-hotels")
    public ResponsePaginatedVO<OrderHotelVO> getOrderHotel(@PathVariable String userCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {

        PageVO pageVO = new PageVO(limit, page);
        return adminService.getOrderHotel(userCode, pageVO);
    }

//    @GetMapping("/hotels/all")
//    public ResponseVO<List<HotelAdminVO>> getHotels(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
//        PageVO pageVO = new PageVO(limit, page);
//        return adminService.getHotels(pageVO);
//    }

    @GetMapping("/hotels/{hotelCode}/rooms")
    public ResponseVO<List<RoomAdminVO>> getRoomsByHotelCode(@PathVariable String hotelCode, @RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return adminService.getRoomsByHotelCode(hotelCode, pageVO);
    }

    @PostMapping("/city/add")
    public ResponseVO<String> addCity(@RequestBody List<CityAddVO> cityVO) {
        return  adminService.addCityCountry(cityVO);
    }

    @PostMapping("/hotel/add")
    public ResponseVO<String> addHotel(@RequestBody HotelApartmentsAddVO hotelAddVO) {
        return adminService.addHotel(hotelAddVO);
    }

    @PostMapping("/apartment/add")
    public ResponseVO<String> addApartment(@RequestBody HotelApartmentsAddVO apartmentAddVO) {
        return adminService.addApartment(apartmentAddVO);
    }

    @PostMapping("/airline/add")
    public ResponseVO<String> addAirline(@RequestBody AirlineAddVO airlineAddVO) {
        return adminService.addAirline(airlineAddVO);
    }

    @GetMapping("/user/getInfo/{userCode}")
    public ResponseVO<UserInfoVO> getUserInfoByCode(@PathVariable String userCode) {
        return userService.getUserInfoByCode(userCode);
    }

    @GetMapping("/airports/all")
    public ResponsePaginatedVO<AirportAdminVO> getAirports(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return adminService.getAirports(pageVO);
    }

    @GetMapping("/city/all")
    public ResponseVO<List<CityVO>> getCities() {
        return adminService.getCities();
    }

    @PostMapping("/airports/create")
    public ResponseVO<String> createAirport(@RequestBody AirportFormVO airportAdminVO) {
        return adminService.createAirport(airportAdminVO);
    }

    @PutMapping("/airports/update/{code}")
    public ResponseVO<String> updateAirport(@PathVariable String code, @RequestBody AirportFormVO airportAdminVO) {
        return adminService.updateAirport(code, airportAdminVO);
    }
}