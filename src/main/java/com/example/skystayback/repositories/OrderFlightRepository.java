package com.example.skystayback.repositories;


import com.example.skystayback.models.OrderFlight;
import com.example.skystayback.models.OrderHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderFlightRepository extends JpaRepository<OrderFlight, Long> {

    boolean existsByCode(String code);
}