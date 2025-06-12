package com.example.skystayback.repositories;


import com.example.skystayback.dtos.meal.MealTableVO;
import com.example.skystayback.models.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("""
        Select new com.example.skystayback.dtos.meal.MealTableVO(
            m.code,
            m.name
        )
        from Meal m
        """)
    Page<MealTableVO> getAllMeals(Pageable pageable);

    Optional<Meal> findByCode(String code);
}