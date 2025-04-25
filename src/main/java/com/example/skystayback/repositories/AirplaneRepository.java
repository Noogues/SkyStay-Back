package com.example.skystayback.repositories;

import com.example.skystayback.dtos.airplanes.AirplaneShowVO;
import com.example.skystayback.models.Airplane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {

    @Query(value = """
        SELECT new com.example.skystayback.dtos.airplanes.AirplaneShowVO(
            a.code,
            a.model,
            a.registrationNumber,
            a.yearOfManufacture,
            a.type,
            a.status,
            new com.example.skystayback.dtos.airplanes.AirplaneTypeVO(
                at.code,
                at.name,
                at.manufacturer,
                at.capacity
            )
        )
        FROM Airplane a JOIN a.airplaneType at
    """)
    Page<AirplaneShowVO> getAllAirplanes(Pageable pageable);
}