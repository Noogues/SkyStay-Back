package com.example.skystayback.repositories;

import com.example.skystayback.dtos.hotel.RoomConfigurationVO;
import com.example.skystayback.enums.RoomType;
import com.example.skystayback.models.Hotel;
import com.example.skystayback.models.RoomConfigurationHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomConfigurationHotelRepository extends JpaRepository<RoomConfigurationHotel, Long> {

    @Query(value = """
                SELECT new com.example.skystayback.dtos.hotel.RoomConfigurationVO(
                    rc.id, rc.capacity, rc.type, rch.url
                )
                FROM Hotel h
                JOIN RoomConfigurationHotel rch on rch.hotel.id = h.id
                join RoomConfiguration rc on rch.roomConfiguration.id = rc.id
                WHERE h.id = :id
            """)
    List<RoomConfigurationVO> findRoomDetailsByHotelId(@Param("id") Long id);

    @Query(value = """
            SELECT rch
            FROM Hotel h
            JOIN RoomConfigurationHotel rch on rch.hotel.id = h.id
            JOIN RoomConfiguration rc on rch.roomConfiguration.id = rc.id
            WHERE h.code = :code AND rc.type = :type
        """)
    RoomConfigurationHotel findByHotelAndRoomType(@Param("code") String code, @Param("type") RoomType type);
}