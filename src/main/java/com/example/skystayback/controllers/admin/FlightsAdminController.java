package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.flights.*;
import com.example.skystayback.enums.FlightStatus;
import com.example.skystayback.services.admin.FlightsAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("admin/flights")
public class FlightsAdminController {

    private final FlightsAdministrationService flightsService;

    @GetMapping("/all")
    public ResponsePaginatedVO<FlightsTableVO> getFlights(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return flightsService.getAllFlights(pageVO);
    }

    @GetMapping("/{code}")
    public ResponseVO<FlightsDetailsVO> getDetailsByCode(@PathVariable String code) {
        return flightsService.getDetailsByCode(code);
    }

    @GetMapping("/all/status")
    public ResponseVO<FlightStatus[]> getAllFlightStatuses() {
        return new ResponseVO<>(new DataVO<>(FlightStatus.values()), new MessageResponseVO("Lista de estados de vuelo recuperada con éxito.", 200, LocalDateTime.now()));
    }

    @PostMapping("/create")
    public ResponseVO<Void> createFlight(@RequestBody FlightCreateVO form) {
        return flightsService.createFlight(form);
    }

    @GetMapping("/generate/random")
    public ResponseVO<Void> generateRandomFlights(@RequestParam Integer amount) {
        return flightsService.generateRandomFlights(amount);
    }

    @GetMapping("/cabins/{code}")
    public ResponseVO<List<CabinInfoVO>> getCabinsByCode(@PathVariable String code) {
        return flightsService.getCabinsByCode(code);
    }

}
