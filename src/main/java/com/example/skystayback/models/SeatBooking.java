package com.example.skystayback.models;

import com.example.skystayback.enums.StatusRoomBooking;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "seatbooking")
public class SeatBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
