package com.example.skystayback.repositories;


import com.example.skystayback.dtos.flights.FlightsDetailsVO;
import com.example.skystayback.dtos.flights.FlightsTableVO;
import com.example.skystayback.models.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
    SELECT new com.example.skystayback.dtos.flights.FlightsTableVO(
        f.code,
        f.departureTime,
        f.status,
        f.dateTime,
        f.dateTimeArrival,
        new com.example.skystayback.dtos.flights.AirlineTableVO(
            al.code,
            al.name,
            al.iataCode
        ),
        new com.example.skystayback.dtos.flights.AirportTableVO(
            da.code,
            da.name,
            da.iataCode,
            new com.example.skystayback.dtos.city.CityVO(
                dac.name,
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
                    aco.name
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
    JOIN f.airline al
    JOIN f.departureAirport da
    JOIN da.city dac
    JOIN dac.country dco
    JOIN f.arrivalAirport aa
    JOIN aa.city aac
    JOIN aac.country aco
    JOIN f.airplane ap
""")
    Page<FlightsTableVO> findAllFlights(Pageable pageable);



    @Query("""
    SELECT new com.example.skystayback.dtos.flights.FlightsDetailsVO(
        f.code,
        f.departureTime,
        f.status,
        f.dateTime,
        f.dateTimeArrival,
        new com.example.skystayback.dtos.airline.AirlineTableVO(
            al.code,
            al.name,
            al.image,
            al.phone,
            al.email,
            al.website,
            al.iataCode
        ) as airlane,
        new com.example.skystayback.dtos.airports.AirportAdminVO(
            da.code,
            da.name,
            da.iataCode,
            da.description,
            da.terminal,
            da.gate,
            da.latitude,
            da.longitude,
            da.timezone,
            new com.example.skystayback.dtos.city.CityVO(
                dac.name,
                new com.example.skystayback.dtos.city.CountryVO(
                    dco.name
                ) as country
            ) as city
        ) as depatureAirport,
        new com.example.skystayback.dtos.airports.AirportAdminVO(
            aa.code,
            aa.name,
            aa.iataCode,
            aa.description,
            aa.terminal,
            aa.gate,
            aa.latitude,
            aa.longitude,
            aa.timezone,
            new com.example.skystayback.dtos.city.CityVO(
                dac.name,
                new com.example.skystayback.dtos.city.CountryVO(
                    dco.name
                ) as country
            ) as city
        ) as arrivalAirport,
        new com.example.skystayback.dtos.flights.AirplaneShowVO(
            ap.code,
            ap.model,
            ap.registrationNumber,
            ap.yearOfManufacture,
            ap.type,
            ap.status,
            new com.example.skystayback.dtos.airplanes.AirplaneTypeVO(
                aap.code,
                aap.name,
                aap.manufacturer,
                aap.capacity
            ) as airplaneType
        ) as airplane
    )
    FROM Flight f
    JOIN f.airline al
    JOIN f.departureAirport da
    JOIN da.city dac
    JOIN dac.country dco
    JOIN f.arrivalAirport aa
    JOIN aa.city aac
    JOIN aac.country aco
    JOIN f.airplane ap
    join ap.airplaneType aap
    WHERE f.code = :code
""")
    FlightsDetailsVO findFlightDetailsByCode(@Param("code") String code);


    @Query("SELECT f FROM Flight f WHERE f.airline.id = :airlineId " +
            "AND f.departureAirport.id = :depatureAirportId " +
            "AND f.arrivalAirport.id = :arrivalAirportId " +
            "AND f.airplane.id = :airplaneId " +
            "AND f.dateTime BETWEEN :start AND :end")
    List<Flight> findSimilarFlights(Long airlineId, Long depatureAirportId, Long arrivalAirportId,
                                    Long airplaneId, LocalDateTime start, LocalDateTime end);



    @Query("""
    SELECT new com.example.skystayback.dtos.flights.FlightsDetailsVO(
        f.code,
        f.departureTime,
        f.status,
        f.dateTime,
        f.dateTimeArrival,
        new com.example.skystayback.dtos.airline.AirlineTableVO(
            al.code,
            al.name,
            al.image,
            al.phone,
            al.email,
            al.website,
            al.iataCode
        ) as airlane,
        new com.example.skystayback.dtos.airports.AirportAdminVO(
            da.code,
            da.name,
            da.iataCode,
            da.description,
            da.terminal,
            da.gate,
            da.latitude,
            da.longitude,
            da.timezone,
            new com.example.skystayback.dtos.city.CityVO(
                dac.name,
                new com.example.skystayback.dtos.city.CountryVO(
                    dco.name
                ) as country
            ) as city
        ) as depatureAirport,
        new com.example.skystayback.dtos.airports.AirportAdminVO(
            aa.code,
            aa.name,
            aa.iataCode,
            aa.description,
            aa.terminal,
            aa.gate,
            aa.latitude,
            aa.longitude,
            aa.timezone,
            new com.example.skystayback.dtos.city.CityVO(
                dac.name,
                new com.example.skystayback.dtos.city.CountryVO(
                    dco.name
                ) as country
            ) as city
        ) as arrivalAirport,
        new com.example.skystayback.dtos.flights.AirplaneShowVO(
            ap.code,
            ap.model,
            ap.registrationNumber,
            ap.yearOfManufacture,
            ap.type,
            ap.status,
            new com.example.skystayback.dtos.airplanes.AirplaneTypeVO(
                aap.code,
                aap.name,
                aap.manufacturer,
                aap.capacity
            ) as airplaneType
        ) as airplane
    )
    FROM Flight f
    JOIN f.airline al
    JOIN f.departureAirport da
    JOIN da.city dac
    JOIN dac.country dco
    JOIN f.arrivalAirport aa
    JOIN aa.city aac
    JOIN aac.country aco
    JOIN f.airplane ap
    join ap.airplaneType aap
    ORDER BY f.id DESC
    limit 5
    
""")
    List<FlightsDetailsVO> getLast5Flights();

    @Query("SELECT f FROM Flight f")
    List<Flight> totalFlightsScheduled();
}