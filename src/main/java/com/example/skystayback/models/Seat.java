package com.example.skystayback.models;

import com.example.skystayback.enums.SeatClass;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "country")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "seat_row", nullable = false, length = 10, unique = true)
    private String seat_row;

    @Column(name = "seat_column", nullable = false, length = 10, unique = true)
    private String seat_column;

    @Column(name = "continent", nullable = false, length = 100)
    private String continent;

    @Column(name = "class", nullable = false, length = 100)
    @Enumerated(EnumType.ORDINAL)
    private SeatClass seat_class;

    //True si esta ocupado, False si esta libre.
    @Column(name = "state", nullable = false)
    private Boolean state;

}
