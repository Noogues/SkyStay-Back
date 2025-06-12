package com.example.skystayback.repositories;

import com.example.skystayback.enums.StatusRoomBooking;
import com.example.skystayback.models.RoomBooking;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    /**
     * Devuelve la lista de fechas libres entre startDate y endDate
     * para cualquiera de las rooms que pertenezcan a las roomConfigurationHotel
     * cuyo id está en :roomConfigIds.
     *
     * Nota: endDate **no** se incluye (la última noche disponible es endDate - 1 día).
     */
    @Query(value = """
        WITH RECURSIVE all_dates AS (
          SELECT CAST(:startDate AS DATE) AS dt
          UNION ALL
          SELECT DATE_ADD(dt, INTERVAL 1 DAY) 
            FROM all_dates 
           WHERE dt < :endDate
        )
        SELECT dt
          FROM all_dates d
         WHERE NOT EXISTS (
               SELECT 1
                 FROM room_booking rb
                 JOIN room r 
                   ON rb.room_id = r.id
                 JOIN room_configuration_hotel rch
                   ON r.room_configuration_hotel_id = rch.id
                WHERE rch.id IN (:roomConfigIds)
                  AND d.dt BETWEEN rb.start_date 
                                AND DATE_SUB(rb.end_date, INTERVAL 1 DAY)
             )
         ORDER BY dt
        """,
            nativeQuery = true
    )
    List<java.sql.Date> findAvailableDates(
            @Param("roomConfigIds") List<Long> roomConfigIds,
            @Param("startDate")    java.sql.Date startDate,
            @Param("endDate")      java.sql.Date endDate
    );

    @Query(value = """
        WITH RECURSIVE all_dates AS (
          SELECT CAST(:startDate AS DATE) AS dt
          UNION ALL
          SELECT DATE_ADD(dt, INTERVAL 1 DAY)
            FROM all_dates
           WHERE dt < :endDate
        )
        SELECT dt
          FROM all_dates d
         WHERE NOT EXISTS (
               SELECT 1
                 FROM room_booking_apartment rba
                 JOIN room_apartment ra
                   ON rba.room_apartment_id = ra.id
                 JOIN room_configuration_apartment rca
                   ON ra.room_configuration_apartment_id = rca.id
                WHERE rca.id IN (:roomConfigIds)
                  AND d.dt BETWEEN rba.start_date
                               AND DATE_SUB(rba.end_date, INTERVAL 1 DAY)
             )
         ORDER BY dt
        """,
            nativeQuery = true
    )
    List<java.sql.Date> findAvailableDatesAp(
            @Param("roomConfigIds") List<Long> roomConfigIds,
            @Param("startDate")    java.sql.Date startDate,
            @Param("endDate")      java.sql.Date endDate
    );
}
