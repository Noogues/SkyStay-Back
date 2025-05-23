package com.example.skystayback.repositories;

import com.example.skystayback.dtos.hotel.RoomVO;
import com.example.skystayback.models.Room;
import com.example.skystayback.models.RoomApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomApartmentRepository extends JpaRepository<RoomApartment, Long> {


    @Query(value = """
             SELECT new com.example.skystayback.dtos.hotel.RoomVO(
                ra.room_number as roomNumber,
                ra.state
             )
             FROM RoomApartment ra
             INNER JOIN RoomConfigurationApartment rca ON ra.roomConfiguration.id = rca.id
             INNER JOIN RoomConfiguration rc ON rc.id = rca.roomConfiguration.id
             WHERE rc.id = :roomConfigurationId
            """)
    List<RoomVO> findAllRoomsByApartmentCodeAndRoomConfigurationId(@Param("roomConfigurationId") Long roomConfigurationId);
}