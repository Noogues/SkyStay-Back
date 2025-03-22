package com.example.skystayback.models;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;
import java.time.Year;

@Data
@Entity
@Table(name = "airplane")
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "registration_number", nullable = false, length = 20, unique = true)
    private String registration_number;

    @Column(name = "manufacturer", nullable = false, length = 200)
    private String manufacturer;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "year_of_manufacture", nullable = false)
    private Year year_of_manufacture;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AirplaneTypeEnum type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "airplane_type_id")
    private AirplaneType airplaneType;


}
