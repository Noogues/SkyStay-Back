package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "airport")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, length = 16, unique = true)
    private String code;

    @Column(name = "iata_code", nullable = false, length = 3, unique = true)
    private String iataCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "terminal", nullable = false, length = 50)
    private String terminal;

    @Column(name = "gate", nullable = false, length = 50)
    private String gate;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

}
