package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageReducedVO;
import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageTableVO;
import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.services.admin.AdditionalBaggageAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/additional-baggage")
public class AdditionalBaggageAdminController {

    private final AdditionalBaggageAdministrationService additionalBaggageService;


    @GetMapping("/all")
    public ResponsePaginatedVO<AdditionalBaggageTableVO> getAllAdditionalBaggages(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return additionalBaggageService.getAllAdditionalBaggages(pageVO);
    }

    @GetMapping("/all/reduced")
    public ResponsePaginatedVO<AdditionalBaggageReducedVO> getAllAdditionalBaggagesReduced(@RequestParam(defaultValue = "100") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return additionalBaggageService.getAllAdditionalBaggagesReduced(pageVO);
    }

    @PostMapping("/create")
    public ResponseVO<Void> createAdditionalBaggage(@RequestBody AdditionalBaggageVO form) {
        return additionalBaggageService.createAdditionalBaggage(form);
    }
}
