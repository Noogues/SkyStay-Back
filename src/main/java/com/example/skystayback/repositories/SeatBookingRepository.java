package com.example.skystayback.repositories;

import com.example.skystayback.models.FlightSeatStatus;
import com.example.skystayback.models.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {
    boolean existsByFlightSeatStatusAndEmailAndNifAndPhone(
            FlightSeatStatus flightSeatStatus, String email, String nif, String phone);
}