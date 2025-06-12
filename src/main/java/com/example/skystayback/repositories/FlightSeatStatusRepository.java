package com.example.skystayback.repositories;


import com.example.skystayback.enums.SeatClass;
import com.example.skystayback.models.FlightSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FlightSeatStatusRepository extends JpaRepository<FlightSeatStatus, Long> {

    @Query("SELECT fss FROM FlightSeatStatus fss JOIN fss.flight f JOIN fss.seat s WHERE f.code = :flightCode AND s.seatRow = :seatRow AND s.seatColumn = :seatColumn AND s.cabin.seatClass = :seatClass")
    Optional<FlightSeatStatus> findSeatByFlightCodeAndRowAndColumn(@Param("flightCode") String flightCode, @Param("seatRow") String seatRow, @Param("seatColumn") String seatColumn, @Param("seatClass") SeatClass seatClass);
}