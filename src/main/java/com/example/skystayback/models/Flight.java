package com.example.skystayback.models;

import com.example.skystayback.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departure_time;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private FlightStatus status;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime date_time;

    @Column(name = "date_time_arrival", nullable = false)
    private LocalDateTime date_time_arrival;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_airport_id")
    private Airport depature_airport;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrival_airport;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;

}
