package com.example.skystayback.repositories;


import com.example.skystayback.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

   Optional<Airport> findByCode(String code);
}