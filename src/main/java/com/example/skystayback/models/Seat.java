package com.example.skystayback.models;

import com.example.skystayback.enums.SeatClass;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "seat_row", nullable = false, length = 10)
    private String seatRow;

    @Column(name = "seat_column", nullable = false, length = 10)
    private String seatColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_cabin_id", nullable = false)
    private AirplaneCabin cabin;
}
