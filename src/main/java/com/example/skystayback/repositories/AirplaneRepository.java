package com.example.skystayback.repositories;

import com.example.skystayback.dtos.airplanes.AirplaneAllCodeVO;
import com.example.skystayback.dtos.airplanes.AirplaneReducedVO;
import com.example.skystayback.dtos.airplanes.AirplaneShowVO;
import com.example.skystayback.models.Airplane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


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
            i.url,
            new com.example.skystayback.dtos.airplanes.AirplaneTypeVO(
                at.code,
                at.name,
                at.manufacturer,
                at.capacity
            )
        )
        FROM Airplane a JOIN a.airplaneType at
        left join AirplaneImage ai on a.id = ai.airplane.id
        left join Image i on ai.image.id = i.id
        ORDER BY a.id ASC
    """)
    Page<AirplaneShowVO> getAllAirplanes(Pageable pageable);

    @Query(value = """
    SELECT new com.example.skystayback.dtos.airplanes.AirplaneAllCodeVO(
        a.model,
        a.registrationNumber,
        a.yearOfManufacture,
        a.type,
        a.status,
        at.name,
        at.manufacturer,
        at.capacity
    )
    FROM Airplane a
    JOIN a.airplaneType at
    WHERE a.code = :airplaneCode
""")
    AirplaneAllCodeVO findBasicInfoByCode(String airplaneCode);

    Optional<Airplane> findByCode(String code);

    @Query(value = """
        SELECT new com.example.skystayback.dtos.airplanes.AirplaneReducedVO(
            a.id,
            a.model
        )
        FROM Airplane a
    """)
    Page<AirplaneReducedVO> getAllAirplanesReduced(Pageable pageable);
}