package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "flight_seat_status")
public class FlightSeatStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "state", nullable = false)
    private Boolean state;

    @Column(name = "price", nullable = false)
    private Float price;
}
