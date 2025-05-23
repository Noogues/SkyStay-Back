package com.example.skystayback.repositories;


import com.example.skystayback.dtos.hotel.*;
import com.example.skystayback.enums.RoomType;
import com.example.skystayback.models.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


    @Query("SELECT new com.example.skystayback.dtos.hotel.HotelAdminVO(h.code, h.name, h.address, h.postal_code, h.phone_number, h.email, h.website, h.description, h.stars, i.url, " + "new com.example.skystayback.dtos.city.CityVO(c.name, new com.example.skystayback.dtos.city.CountryVO(c.country.name))) FROM Hotel h JOIN h.city c LEFT JOIN HotelImage hi ON hi.hotel.id = h.id LEFT JOIN Image i ON hi.image.id = i.id")
    Page<HotelAdminVO> findAllHotels(Pageable pageable);

    Optional<Hotel> findByCode(String code);


    @Query("""
                SELECT new com.example.skystayback.dtos.hotel.ShowHotelDetails(
                    h.id, h.name, h.address, h.postal_code, h.phone_number, h.email, h.website, h.stars, h.description,
                    c.name, c.country.name, i.url, null
                )
                FROM Hotel h
                JOIN h.city c
                LEFT JOIN HotelImage hi ON hi.hotel.id = h.id
                LEFT JOIN Image i ON hi.image.id = i.id
                WHERE h.code = :hotelCode
            """)
    ShowHotelDetails findHotelDetailsByCode(@Param("hotelCode") String hotelCode);
}