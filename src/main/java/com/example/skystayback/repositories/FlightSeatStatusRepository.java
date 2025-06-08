package com.example.skystayback.repositories;


import com.example.skystayback.dtos.flights.FlightClientVO;
import com.example.skystayback.dtos.flights.FlightsDetailsVO;
import com.example.skystayback.dtos.flights.FlightsTableVO;
import com.example.skystayback.models.Flight;
import com.example.skystayback.models.FlightSeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface FlightSeatStatusRepository extends JpaRepository<FlightSeatStatus, Long> {


}