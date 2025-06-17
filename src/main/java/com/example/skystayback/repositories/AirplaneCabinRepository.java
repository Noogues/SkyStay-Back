package com.example.skystayback.repositories;


import com.example.skystayback.dtos.flights.CabinsVO;
import com.example.skystayback.models.AirplaneCabin;
import com.example.skystayback.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirplaneCabinRepository extends JpaRepository<AirplaneCabin, Long> {


    List<AirplaneCabin> findAllByAirplaneCode(String airplaneCode);

    @Query("SELECT new com.example.skystayback.dtos.flights.CabinsVO(ac.id, ac.seatClass) FROM AirplaneCabin ac WHERE ac.airplane.id = :airplaneId")
    List<CabinsVO> getCabinsAirplaneId(@Param("airplaneId") Long airplaneId);

    List<AirplaneCabin> findByAirplaneId(Long id);
}