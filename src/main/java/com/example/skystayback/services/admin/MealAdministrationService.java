package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.meal.MealTableVO;
import com.example.skystayback.dtos.meal.MealVO;
import com.example.skystayback.models.Meal;
import com.example.skystayback.repositories.MealRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MealAdministrationService {

    private final MealRepository mealRepository;
    private final UserService userService;

    /**
     * Obtiene todos los platos disponibles en la base de datos.
     * @param pageVO Objeto que contiene la información de paginación.
     * @return Un objeto ResponsePaginatedVO que contiene la lista de platos y la información de paginación.
     */
    public ResponsePaginatedVO<MealTableVO> getAllMeals(PageVO pageVO) {
        try {
            Page<MealTableVO> meal = mealRepository.getAllMeals(pageVO.toPageable());
            ResponsePaginatedVO<MealTableVO> data = new ResponsePaginatedVO<>();
            data.setObjects(meal.getContent());
            data.setHasNextPage(meal.hasNext());
            data.setHasPreviousPage(meal.hasPrevious());
            data.setCurrentPage(meal.getNumber());
            data.setTotalPages(meal.getTotalPages());
            data.setMessages(new MessageResponseVO("Platos recuperados con exito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllMeals: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los platos.", 404, LocalDateTime.now()));
        }
    }

    /**
     * Crea un nuevo plato en la base de datos.
     * @param mealVO Objeto que contiene la información del plato a crear.
     * @return Un objeto ResponseVO que indica el resultado de la operación.
     */
    public ResponseVO<Void> createMeal(MealVO mealVO) {
        try {
            Meal meal = new Meal();
            meal.setCode(userService.generateShortUuid());
            meal.setName(mealVO.getName());
            mealRepository.save(meal);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Plato creado con exito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("createMeal: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear el plato", 404, LocalDateTime.now()));
        }
    }
}
