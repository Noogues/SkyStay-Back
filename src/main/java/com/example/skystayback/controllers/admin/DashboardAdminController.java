package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.flights.FlightsDetailsVO;
import com.example.skystayback.dtos.user.UserAdminVO;
import com.example.skystayback.services.admin.DashboardAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("admin/dashboard")
public class DashboardAdminController {
    private final DashboardAdministrationService dashboardAdminService;

    @GetMapping("/total-revenue")
    public ResponseVO<Double> getTotalRevenue() {
        return dashboardAdminService.getTotalRevenue();
    }

    @GetMapping("/last-users")
    public ResponseVO<List<UserAdminVO>> getLast5User() {
        return dashboardAdminService.getLast5User();
    }

    @GetMapping("/last-flights")
    public ResponseVO<List<FlightsDetailsVO>> getLast5Flights() {
        return dashboardAdminService.getLast5Flights();
    }

    @GetMapping("/total-flights-active")
    public ResponseVO<Integer> getTotalFlightsActive() {
        return dashboardAdminService.getTotalFlightsActive();
    }

    @GetMapping("/total-users")
    public ResponseVO<Integer> totalUsersAcounts() {
        return dashboardAdminService.totalUsersAcounts();
    }
}
