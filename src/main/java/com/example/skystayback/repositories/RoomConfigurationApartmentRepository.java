package com.example.skystayback.repositories;

import com.example.skystayback.dtos.hotel.RoomConfigurationVO;
import com.example.skystayback.enums.RoomType;
import com.example.skystayback.models.RoomConfigurationApartment;
import com.example.skystayback.models.RoomConfigurationHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomConfigurationApartmentRepository extends JpaRepository<RoomConfigurationApartment, Long> {

    @Query(value = """
                SELECT new com.example.skystayback.dtos.hotel.RoomConfigurationVO(
                    rc.id, rc.capacity, rc.type, rch.url
                )
                FROM Apartment a
                JOIN RoomConfigurationApartment rch on rch.apartment.id = a.id
                join RoomConfiguration rc on rch.roomConfiguration.id = rc.id
                WHERE a.id = :id
            """)
    List<RoomConfigurationVO> findRoomDetailsByApartmentId(@Param("id") Long id);

    @Query(value = """
            SELECT rch
            FROM Apartment a
            JOIN RoomConfigurationApartment rch on rch.apartment.id = a.id
            JOIN RoomConfiguration rc on rch.roomConfiguration.id = rc.id
            WHERE a.code = :code AND rc.type = :type
        """)
    RoomConfigurationApartment findByApartmentAndRoomType(@Param("code") String code, @Param("type") RoomType type);
}