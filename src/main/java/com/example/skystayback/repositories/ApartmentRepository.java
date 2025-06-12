package com.example.skystayback.repositories;


import com.example.skystayback.dtos.hotel.HotelAdminVO;
import com.example.skystayback.dtos.hotel.ShowHotelDetails;
import com.example.skystayback.models.Apartment;
import com.example.skystayback.models.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {



    @Query("SELECT new com.example.skystayback.dtos.hotel.HotelAdminVO(a.code, a.name, a.address, a.postalCode, a.phoneNumber, a.email, a.website, a.description, a.stars, i.url, " + "new com.example.skystayback.dtos.city.CityVO(c.name, new com.example.skystayback.dtos.city.CountryVO(c.country.name))) FROM Apartment a JOIN a.city c LEFT JOIN ApartmentImage ai ON ai.apartment.id = a.id LEFT JOIN Image i ON ai.image.id = i.id")
    Page<HotelAdminVO> findAllApartments(Pageable pageable);

    Optional<Apartment> findByCode(String code);


    @Query("""
                SELECT new com.example.skystayback.dtos.hotel.ShowHotelDetails(
                    a.id, a.name, a.address, a.postalCode, a.phoneNumber, a.email, a.website, a.stars, a.description,
                    c.name, c.country.name, i.url, null
                )
                FROM Apartment a
                JOIN a.city c
                LEFT JOIN ApartmentImage ai ON ai.apartment.id = a.id
                LEFT JOIN Image i ON ai.image.id = i.id
                WHERE a.code = :hotelCode
            """)
    ShowHotelDetails findApartmentDetailsByCode(@Param("hotelCode") String hotelCode);

//    @Query("""
//          SELECT a FROM Apartment a
//          JOIN a.city c
//          WHERE (:destination IS NULL OR c.name LIKE %:destination% OR a.address LIKE %:destination%)
//          AND a.capacity >= :adults + :children
//          """)
//    List<Apartment> findByFilters(
//            @Param("destination") String destination,
//            @Param("checkIn") LocalDate checkIn,
//            @Param("checkOut") LocalDate checkOut,
//            @Param("adults") Integer adults,
//            @Param("children") Integer children,
//            @Param("rooms") Integer rooms
//    );
}