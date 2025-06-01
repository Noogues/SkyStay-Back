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
       c.name
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
    COUNT(r.id)
)
FROM Room r
JOIN r.roomConfiguration rch
JOIN rch.hotel h
LEFT JOIN RoomBooking rb ON rb.room.id = r.id
AND rb.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING')
AND (:checkOut IS NULL OR rb.startDate < :checkOut)
AND (:checkIn IS NULL OR rb.endDate > :checkIn)
WHERE h.id = :hotelId
AND rb.id IS NULL
GROUP BY rch.id
HAVING rch.roomConfiguration.capacity >= (:adults + :children)
AND COUNT(r.id) >= :rooms
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
SELECT new com.example.skystayback.dtos.common.DestinationVO(h.id, h.name, i.url)
FROM Hotel h
LEFT JOIN HotelImage hi ON hi.hotel.id = h.id
LEFT JOIN Image i ON hi.image.id = i.id
JOIN HotelRating hr ON hr.hotel.id = h.id
GROUP BY h.id, h.name, i.url
ORDER BY AVG(hr.rating) DESC
""")
    List<DestinationVO> findTopRatedHotels(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(a.id, a.name, i.url)
FROM Apartment a
LEFT JOIN ApartmentImage ai ON ai.apartment.id = a.id
LEFT JOIN Image i ON ai.image.id = i.id
JOIN ApartmentRating ar ON ar.apartment.id = a.id
GROUP BY a.id, a.name, i.url
ORDER BY AVG(ar.rating) DESC
""")
    List<DestinationVO> findTopRatedApartments(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(h.id, h.name, i.url)
FROM Hotel h
LEFT JOIN HotelImage hi ON hi.hotel.id = h.id
LEFT JOIN Image i ON hi.image.id = i.id
GROUP BY h.id, h.name, i.url
""")
    List<DestinationVO> findRandomHotels(Pageable pageable);

    @Query("""
SELECT new com.example.skystayback.dtos.common.DestinationVO(a.id, a.name, i.url)
FROM Apartment a
LEFT JOIN ApartmentImage ai ON ai.apartment.id = a.id
LEFT JOIN Image i ON ai.image.id = i.id
GROUP BY a.id, a.name, i.url
""")
    List<DestinationVO> findRandomApartments(Pageable pageable);
}