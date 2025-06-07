package com.example.skystayback.repositories;


import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.hotel.*;

import com.example.skystayback.models.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.skystayback.dtos.common.AccommodationResponseVO;
import com.example.skystayback.dtos.common.RoomDetailsVO;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


    @Query("SELECT new com.example.skystayback.dtos.hotel.HotelAdminVO(h.code, h.name, h.address, h.postalCode, h.phoneNumber, h.email, h.website, h.description, h.stars, i.url, " + "new com.example.skystayback.dtos.city.CityVO(c.name, new com.example.skystayback.dtos.city.CountryVO(c.country.name))) FROM Hotel h JOIN h.city c LEFT JOIN HotelImage hi ON hi.hotel.id = h.id LEFT JOIN Image i ON hi.image.id = i.id")
    Page<HotelAdminVO> findAllHotels(Pageable pageable);

    Optional<Hotel> findByCode(String code);


    @Query("""
                SELECT new com.example.skystayback.dtos.hotel.ShowHotelDetails(
                    h.id, h.name, h.address, h.postalCode, h.phoneNumber, h.email, h.website, h.stars, h.description,
                    c.name, c.country.name, i.url, null
                )
                FROM Hotel h
                JOIN h.city c
                LEFT JOIN HotelImage hi ON hi.hotel.id = h.id
                LEFT JOIN Image i ON hi.image.id = i.id
                WHERE h.code = :hotelCode
            """)
    ShowHotelDetails findHotelDetailsByCode(@Param("hotelCode") String hotelCode);

    @Query("""
            SELECT new com.example.skystayback.dtos.common.AccommodationResponseVO(
                h.id,
                h.name,
                h.stars,
                h.address,
                h.phoneNumber,
                h.email,
                h.website,
                h.description,
                c.name,
                (SELECT MIN(i.url) FROM HotelImage hi JOIN Image i ON hi.image.id = i.id WHERE hi.hotel.id = h.id)
            )
            FROM Hotel h
            JOIN h.city c
            WHERE c.name = :destination
            """)
    List<AccommodationResponseVO> findHotelsByDestination(@Param("destination") String destination);

    @Query("""
    SELECT new com.example.skystayback.dtos.common.RoomDetailsVO(
        rch.id,
        rch.roomConfiguration.capacity,
        rch.roomConfiguration.type,
        (rch.amount - COALESCE(
            (SELECT COUNT(rb2.id)
            FROM RoomBooking rb2
            JOIN rb2.room r2
            WHERE r2.roomConfiguration.id = rch.id
            AND rb2.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
            AND (:checkOut IS NULL OR rb2.startDate < :checkOut)
            AND (:checkIn IS NULL OR rb2.endDate > :checkIn)), 0
        )),
        rch.price
    )
    FROM RoomConfigurationHotel rch
    JOIN rch.hotel h
    WHERE h.id = :hotelId
    GROUP BY rch.id, rch.amount, rch.roomConfiguration.capacity, rch.roomConfiguration.type, rch.price
    HAVING (rch.amount - COALESCE(
            (SELECT COUNT(rb3.id)
            FROM RoomBooking rb3
            JOIN rb3.room r3
            WHERE r3.roomConfiguration.id = rch.id
            AND rb3.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
            AND (:checkOut IS NULL OR rb3.startDate < :checkOut)
            AND (:checkIn IS NULL OR rb3.endDate > :checkIn)), 0
        )) >= :rooms
    AND (
        (:rooms = 1 AND rch.roomConfiguration.capacity >= (:adults + :children))
        OR
        (:rooms > 1 AND (
            (rch.roomConfiguration.capacity * :rooms >= (:adults + :children))
            OR
            (rch.roomConfiguration.capacity * (rch.amount - COALESCE(
                (SELECT COUNT(rb4.id)
                FROM RoomBooking rb4
                JOIN rb4.room r4
                WHERE r4.roomConfiguration.id = rch.id
                AND rb4.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
                AND (:checkOut IS NULL OR rb4.startDate < :checkOut)
                AND (:checkIn IS NULL OR rb4.endDate > :checkIn)), 0
            )) >= (:adults + :children))
        ))
    )
""")
    List<RoomDetailsVO> findAvailableRoomsByHotel(
            @Param("hotelId") Long hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("rooms") Integer rooms,
            @Param("adults") Integer adults,
            @Param("children") Integer children
    );

    @Query("SELECT DISTINCT c.name FROM City c")
    List<String> findAllCities();

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(h.id, h.name,
    (SELECT MIN(i2.url) FROM HotelImage hi2 JOIN Image i2 ON hi2.image.id = i2.id WHERE hi2.hotel.id = h.id))
FROM Hotel h
JOIN HotelRating hr ON hr.hotel.id = h.id
GROUP BY h.id, h.name
ORDER BY AVG(hr.rating) DESC
""")
    List<DestinationVO> findTopRatedHotels(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(a.id, a.name,
    (SELECT MIN(i2.url) FROM ApartmentImage ai2 JOIN Image i2 ON ai2.image.id = i2.id WHERE ai2.apartment.id = a.id))
FROM Apartment a
JOIN ApartmentRating ar ON ar.apartment.id = a.id
GROUP BY a.id, a.name
ORDER BY AVG(ar.rating) DESC
""")
    List<DestinationVO> findTopRatedApartments(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(h.id, h.name, 
    (SELECT MIN(i2.url) FROM HotelImage hi2 JOIN Image i2 ON hi2.image.id = i2.id WHERE hi2.hotel.id = h.id))
FROM Hotel h
GROUP BY h.id, h.name
""")
    List<DestinationVO> findRandomHotels(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(a.id, a.name,
    (SELECT MIN(i2.url) FROM ApartmentImage ai2 JOIN Image i2 ON ai2.image.id = i2.id WHERE ai2.apartment.id = a.id))
FROM Apartment a
GROUP BY a.id, a.name
""")
    List<DestinationVO> findRandomApartments(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.AccommodationDetailVO(
    h.id,
    h.name,
    h.stars,
    h.address,
    h.postalCode,
    h.phoneNumber,
    h.email,
    h.website,
    h.description,
    c.name,
    c.country.name,
    h.amenities
)
FROM Hotel h
JOIN h.city c
WHERE h.id = :hotelId
""")
    AccommodationDetailVO findHotelDetailById(@Param("hotelId") Long hotelId);


    @Query("""
SELECT i.url
FROM HotelImage hi
JOIN Image i ON hi.image.id = i.id
WHERE hi.hotel.id = :hotelId
""")
    List<String> findAllHotelImages(@Param("hotelId") Long hotelId);

    @Query("""
        SELECT new com.example.skystayback.dtos.common.AccommodationResponseVO(
            a.id,
            a.name,
            0,
            a.address,
            a.phoneNumber,
            a.email,
            a.website,
            a.description,
            c.name,
            (SELECT MIN(i.url) FROM ApartmentImage ai JOIN Image i ON ai.image.id = i.id WHERE ai.apartment.id = a.id)
        )
        FROM Apartment a
        JOIN a.city c
        WHERE c.name = :destination
        """)
    List<AccommodationResponseVO> findApartmentsByDestination(@Param("destination") String destination);

    @Query("""
    SELECT new com.example.skystayback.dtos.common.RoomDetailsVO(
        rca.id,
        rca.roomConfiguration.capacity,
        rca.roomConfiguration.type,
        (rca.amount - COALESCE(
            (SELECT COUNT(rb2.id)
            FROM RoomBooking rb2
            JOIN rb2.room r2
            WHERE r2.roomConfiguration.id = rca.id
            AND rb2.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
            AND (:checkOut IS NULL OR rb2.startDate < :checkOut)
            AND (:checkIn IS NULL OR rb2.endDate > :checkIn)), 0
        )),
        rca.price
    )
    FROM RoomConfigurationApartment rca
    JOIN rca.apartment a
    WHERE a.id = :apartmentId
    GROUP BY rca.id, rca.amount, rca.roomConfiguration.capacity, rca.roomConfiguration.type, rca.price
    HAVING (rca.amount - COALESCE(
            (SELECT COUNT(rb3.id)
            FROM RoomBooking rb3
            JOIN rb3.room r3
            WHERE r3.roomConfiguration.id = rca.id
            AND rb3.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
            AND (:checkOut IS NULL OR rb3.startDate < :checkOut)
            AND (:checkIn IS NULL OR rb3.endDate > :checkIn)), 0
        )) >= :rooms
    AND (
        (:rooms = 1 AND rca.roomConfiguration.capacity >= (:adults + :children))
        OR
        (:rooms > 1 AND (
            (rca.roomConfiguration.capacity * :rooms >= (:adults + :children))
            OR
            (rca.roomConfiguration.capacity * (rca.amount - COALESCE(
                (SELECT COUNT(rb4.id)
                FROM RoomBooking rb4
                JOIN rb4.room r4
                WHERE r4.roomConfiguration.id = rca.id
                AND rb4.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
                AND (:checkOut IS NULL OR rb4.startDate < :checkOut)
                AND (:checkIn IS NULL OR rb4.endDate > :checkIn)), 0
            )) >= (:adults + :children))
        ))
    )
""")
    List<RoomDetailsVO> findAvailableRoomsByApartment(
            @Param("apartmentId") Long apartmentId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("rooms") Integer rooms,
            @Param("adults") Integer adults,
            @Param("children") Integer children
    );

    @Query("""
SELECT new com.example.skystayback.dtos.common.AccommodationDetailVO(
    a.id,
    a.name,
    0,
    a.address,
    a.postalCode,
    a.phoneNumber,
    a.email,
    a.website,
    a.description,
    c.name,
    c.country.name,
    a.amenities
)
FROM Apartment a
JOIN a.city c
WHERE a.id = :apartmentId
""")
    AccommodationDetailVO findApartmentDetailById(@Param("apartmentId") Long apartmentId);

    @Query("""
SELECT i.url
FROM ApartmentImage ai
JOIN Image i ON ai.image.id = i.id
WHERE ai.apartment.id = :apartmentId
""")
    List<String> findAllApartmentImages(@Param("apartmentId") Long apartmentId);
}