package com.example.skystayback.repositories;


import com.example.skystayback.dtos.flights.FlightsTableVO;
import com.example.skystayback.models.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query(value = """
        SELECT new com.example.skystayback.dtos.flights.FlightsTableVO(
            f.code,
            f.departure_time,
            f.status,
            f.date_time,
            f.date_time_arrival,
            new com.example.skystayback.dtos.flights.AirlaneTableVO(
                a.code,
                a.name,
                a.iataCode
            ),
            new com.example.skystayback.dtos.flights.AirportTableVO(
                da.code,
                da.name,
                da.iataCode,
                new com.example.skystayback.dtos.city.CityVO(
                    aac.name,
                    new com.example.skystayback.dtos.city.CountryVO(
                        dco.name
                    )
                )
            ),
            new com.example.skystayback.dtos.flights.AirportTableVO(
                aa.code,
                aa.name,
                aa.iataCode,
                new com.example.skystayback.dtos.city.CityVO(
                    aac.name,
                    new com.example.skystayback.dtos.city.CountryVO(
                        dco.name
                    )
                )
            ),
            new com.example.skystayback.dtos.flights.AirplaneTableVO(
                ap.code,
                ap.model,
                ap.registrationNumber
            )
        )
        
        FROM Flight f
        JOIN f.airline a
        JOIN f.depature_airport da
        JOIN da.city dac
        JOIN dac.country dco
        JOIN f.arrival_airport aa
        JOIN aa.city aac
        JOIN f.airplane ap
        """)
    Page<FlightsTableVO> findAllFlights(Pageable pageable);

}