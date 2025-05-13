package com.example.skystayback.repositories;

import com.example.skystayback.dtos.hotel.RoomConfigurationVO;
import com.example.skystayback.dtos.hotel.RoomVO;
import com.example.skystayback.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {



    @Query(value = """
             SELECT new com.example.skystayback.dtos.hotel.RoomVO(
                r.room_number as roomNumber,\s
                r.state
             )
             FROM RoomConfiguration rc
             LEFT JOIN Room r ON r.roomConfiguration.id = rc.id
             WHERE rc.id = :roomConfigurationId
            \s""")
    List<RoomVO> findAllRoomsByHotelCodeAndRoomConfigurationId(@Param("roomConfigurationId") Long roomConfigurationId);
}