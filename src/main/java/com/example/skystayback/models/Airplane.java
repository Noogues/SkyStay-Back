package com.example.skystayback.models;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "airplane")
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "registration_number", unique = true, nullable = false)
    private String registrationNumber;

    @Column(name = "year_of_manufacture", nullable = false)
    private Integer yearOfManufacture;

    @Column(name = "type", nullable = false)
    private AirplaneTypeEnum type;

    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "airplane_type_id", nullable = false)
    private AirplaneType airplaneType;

    @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AirplaneCabin> cabins = new ArrayList<>();

}
