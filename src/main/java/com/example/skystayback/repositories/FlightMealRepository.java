package com.example.skystayback.repositories;


import com.example.skystayback.models.FlightMeal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightMealRepository extends JpaRepository<FlightMeal, Long> {
}