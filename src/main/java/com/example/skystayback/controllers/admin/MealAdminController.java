package com.example.skystayback.controllers.admin;

import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.meal.MealTableVO;
import com.example.skystayback.dtos.meal.MealVO;
import com.example.skystayback.models.Meal;
import com.example.skystayback.services.admin.MealAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("admin/meal")
public class MealAdminController {

    private final MealAdministrationService mealService;


    @GetMapping("/all")
    public ResponsePaginatedVO<MealTableVO> getAllMeals(@RequestParam(defaultValue = "30") Integer limit, @RequestParam(defaultValue = "0") Integer page) {
        PageVO pageVO = new PageVO(limit, page);
        return mealService.getAllMeals(pageVO);
    }

    @PostMapping("/create")
    public ResponseVO<Void> createMeal(@RequestBody MealVO meal) {
        return mealService.createMeal(meal);
    }
}
