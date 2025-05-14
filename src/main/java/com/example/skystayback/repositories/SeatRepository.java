package com.example.skystayback.repositories;


import com.example.skystayback.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByCabinId(Long id);
}