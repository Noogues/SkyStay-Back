package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seatbooking")
public class SeatBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_seat_status_id")
    private FlightSeatStatus flightSeatStatus;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
