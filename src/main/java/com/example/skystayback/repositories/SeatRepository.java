package com.example.skystayback.repositories;


import com.example.skystayback.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT s FROM Seat s WHERE s.cabin.id = :cabinId")
    List<Seat> findSeatsByCabinId(@Param("cabinId") Long cabinId);
}