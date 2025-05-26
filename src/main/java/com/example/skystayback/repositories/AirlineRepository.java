package com.example.skystayback.repositories;


import com.example.skystayback.dtos.airline.AirlineReducedVO;
import com.example.skystayback.dtos.airline.AirlineTableVO;
import com.example.skystayback.models.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    @Query(""" 
            SELECT new com.example.skystayback.dtos.airline.AirlineTableVO(a.code, a.name, a.image, a.phone, a.email, a.website, a.iataCode) FROM Airline a
    """)
    Page<AirlineTableVO> getAllAirlines(Pageable pageable);

    @Query("""
            SELECT new com.example.skystayback.dtos.airline.AirlineReducedVO(a.id, a.name) FROM Airline a
    """)
    Page<AirlineReducedVO> getAllAirlinesReduced(Pageable pageable);

    Optional<Airline> findByCode(@Param("code") String code);
}