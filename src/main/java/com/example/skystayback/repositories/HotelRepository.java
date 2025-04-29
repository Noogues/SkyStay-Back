package com.example.skystayback.repositories;


import com.example.skystayback.dtos.hotel.RoomAdminVO;
import com.example.skystayback.models.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT new com.example.skystayback.dtos.hotel.RoomAdminVO(r.room_number, r.capacity, r.type, CASE WHEN EXISTS (SELECT 1 FROM RoomBooking rb WHERE rb.room.id = r.id) THEN FALSE ELSE TRUE END) FROM Hotel h INNER JOIN Room r ON r.hotel.id = h.id WHERE h.code = :hotelCode")
    Page<RoomAdminVO> findAllRoomsByHotelCode(@Param("hotelCode") String hotelCode, Pageable pageable);
}