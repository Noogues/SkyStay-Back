package com.example.skystayback.repositories;


import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.dtos.airports.AirportReducedVO;
import com.example.skystayback.models.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

   Optional<Airport> findByCode(String code);

   @Query("SELECT new com.example.skystayback.dtos.airports.AirportAdminVO(a.code, a.name,a.iataCode, a.description, a.terminal, a.gate, a.latitude, a.longitude, a.timezone, new com.example.skystayback.dtos.city.CityVO(c.name, new com.example.skystayback.dtos.city.CountryVO(co.name))) FROM Airport a INNER JOIN City c ON a.city.id = c.id INNER JOIN Country co ON c.country.id = co.id")
   Page<AirportAdminVO> getAllAirports(Pageable pageable);

   @Query("SELECT new com.example.skystayback.dtos.airports.AirportReducedVO(a.id, a.name) FROM Airport a")
    Page<AirportReducedVO> getAllAirportsReduced(Pageable pageable);
}