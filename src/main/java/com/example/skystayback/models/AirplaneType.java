package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "airplane_type")
public class AirplaneType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Este campo de aqui es el codigo de identificacion autogenerado.
    @Column(name = "code", nullable = false, length = 16, unique = true)
    private String code;

    // Este campo de aqui es el codigo del avion: Ej. 320, 737,
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

}
