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

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_flight_id")
    private OrderFlight orderFlight;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "surnames", nullable = false, length = 100)
    private String surnames;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "nif", nullable = false, length = 50)
    private String nif;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
}
