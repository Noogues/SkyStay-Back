package com.example.skystayback.repositories;


import com.example.skystayback.dtos.flights.*;
import com.example.skystayback.models.Flight;
import com.example.skystayback.models.FlightSeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.skystayback.dtos.meal.MealVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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
            SELECT f FROM Flight f WHERE f.status != 3 AND f.status != 4
            """)
    List<Flight> findAllNotLandedCancelled();

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


    @Query("SELECT f FROM Flight f WHERE f.airline.id = :airlineId " + "AND f.departureAirport.id = :depatureAirportId " + "AND f.arrivalAirport.id = :arrivalAirportId " + "AND f.airplane.id = :airplaneId " + "AND f.dateTime BETWEEN :start AND :end")
    List<Flight> findSimilarFlights(Long airlineId, Long depatureAirportId, Long arrivalAirportId, Long airplaneId, LocalDateTime start, LocalDateTime end);


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

    @Query("""
                SELECT new com.example.skystayback.dtos.flights.CabinInfoVO(
                    ca.id,
                    ca.seatClass,
                    COUNT(fss.seat),
                    COUNT(CASE WHEN fss.state = true THEN 1 END),
                    fss.price
                )
                FROM FlightSeatStatus fss
                JOIN fss.seat.cabin ca
                WHERE fss.flight.code = :code
                GROUP BY ca.id, ca.seatClass, fss.price
            """)
    List<CabinInfoVO> findCabinsByFlightCode(String code);

    @Query("""
                SELECT new com.example.skystayback.dtos.flights.FlightClientVO(
                    f.code,
                    al.name,
                    dac.name,
                    da.name,
                    da.iataCode,
                    aac.name,
                    aa.name,
                    aa.iataCode,
                    f.dateTime,
                    f.dateTimeArrival,
                    MIN(fss.price),
                    COUNT(CASE WHEN fss.state = true THEN 1 END)
                )
                FROM FlightSeatStatus fss
                JOIN fss.flight f
                JOIN f.airline al
                JOIN f.departureAirport da
                JOIN da.city dac
                JOIN f.arrivalAirport aa
                JOIN aa.city aac
                WHERE (:origin IS NULL OR dac.name = :origin)
                  AND (:destination IS NULL OR aac.name = :destination)
                  AND (:airline IS NULL OR al.name = :airline)
                  AND (:price IS NULL OR fss.price <= :price)
                  AND fss.state = true AND f.status = 0
                GROUP BY f.code, al.name, dac.name, da.name, da.iataCode,
                         aac.name, aa.name, aa.iataCode, f.dateTime, f.dateTimeArrival
            """)
    Page<FlightClientVO> findAllClientFlightsWithFilters(Pageable pageable, @Param("origin") String origin, @Param("destination") String destination, @Param("airline") String airline, @Param("price") Float price);

    @Query(value = """
                SELECT
                    f.departure_time AS departureTime,
                    f.status AS status,
                    f.date_time AS dateTime,
                    f.date_time_arrival AS dateTimeArrival,
                    al.code AS airlineCode,
                    al.name AS airlineName,
                    al.image AS airlineImage,
                    al.phone AS airlinePhone,
                    al.email AS airlineEmail,
                    al.website AS airlineWebsite,
                    al.iata_code AS airlineIataCode,
                    da.code AS departureAirportCode,
                    da.name AS departureAirportName,
                    da.iata_code AS departureAirportIataCode,
                    da.description AS departureAirportDescription,
                    da.terminal AS departureTerminal,
                    da.gate AS departureGate,
                    da.latitude AS departureLatitude,
                    da.longitude AS departureLongitude,
                    da.timezone AS departureTimezone,
                    dac.name AS departureCityName,
                    dco.name AS departureCountryName,
                    aa.code AS arrivalAirportCode,
                    aa.name AS arrivalAirportName,
                    aa.iata_code AS arrivalAirportIataCode,
                    aa.description AS arrivalAirportDescription,
                    aa.terminal AS arrivalTerminal,
                    aa.gate AS arrivalGate,
                    aa.latitude AS arrivalLatitude,
                    aa.longitude AS arrivalLongitude,
                    aa.timezone AS arrivalTimezone,
                    aac.name AS arrivalCityName,
                    aco.name AS arrivalCountryName,
                    ap.code AS airplaneCode,
                    ap.model AS airplaneModel,
                    ap.registration_number AS airplaneRegistrationNumber,
                    ap.year_of_manufacture AS airplaneYear,
                    apt.capacity AS airplaneCapacity,
                    apt.manufacturer AS airplaneManufacturer,
                    apt.name AS airplaneTypeName
                FROM flight f
                JOIN airline al ON f.airline_id = al.id
                JOIN airport da ON f.departure_airport_id = da.id
                JOIN city dac ON da.city_id = dac.id
                JOIN country dco ON dac.country_id = dco.id
                JOIN airport aa ON f.arrival_airport_id = aa.id
                JOIN city aac ON aa.city_id = aac.id
                JOIN country aco ON aac.country_id = aco.id
                JOIN airplane ap ON f.airplane_id = ap.id
                JOIN airplane_type apt ON ap.airplane_type_id = apt.id
                WHERE f.code = :flightCode
            """, nativeQuery = true)
    Optional<AllDetailsFlightsProjection> findFlightDetailsById(@Param("flightCode") String flightCode);


    @Query("""
        Select new com.example.skystayback.dtos.meal.MealVO(
            m.name
        )
        FROM Meal m
        Join FlightMeal fm ON m.id = fm.meal.id
        Where fm.flight.code = :flightCode
""")
    List<MealVO> findMealsByFlightCode(String flightCode);


    @Query("""
    Select new com.example.skystayback.dtos.flights.CabinFlightDetailsVO(
            ca.id,
            ca.seatClass,
            ca.seatConfiguration.seatPattern,
            COUNT(fss.seat),
            COUNT(CASE WHEN fss.state = true THEN 1 END),
            fss.price
        )from FlightSeatStatus fss
        JOIN fss.seat.cabin ca
        WHERE fss.flight.code = :flightCode
        GROUP BY ca.id, ca.seatClass, fss.price
    """)
    List<CabinFlightDetailsVO> getCabinsForFlightByCode(String flightCode);

    @Query("""
        SELECT new com.example.skystayback.dtos.flights.SeatVO(
            s.id,
            s.seatRow,
            s.seatColumn,
            fss.state
        )
        FROM FlightSeatStatus fss
        JOIN fss.seat s
        WHERE fss.seat.cabin.id = :id
    """)
    List<SeatVO> getSeatsForCabin(Long id);


}