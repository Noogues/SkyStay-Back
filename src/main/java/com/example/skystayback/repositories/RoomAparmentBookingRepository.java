// src/main/java/com/example/skystayback/repositories/RoomAparmentBookingRepository.java
package com.example.skystayback.repositories;

import com.example.skystayback.models.RoomAparmentBooking;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomAparmentBookingRepository extends JpaRepository<RoomAparmentBooking, Long> {
    @Query("""
      SELECT b
        FROM RoomAparmentBooking b
       WHERE b.room.id      IN :roomIds
         AND b.startDate <= :endDate
         AND b.endDate   >= :startDate
    """)
    List<RoomAparmentBooking> findOverlappingForRooms(
            @Param("roomIds")     List<Long>   roomIds,
            @Param("startDate")   LocalDate    startDate,
            @Param("endDate")     LocalDate    endDate
    );
}
