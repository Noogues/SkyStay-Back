package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "iso_code", nullable = false, length = 3, unique = true)
    private String iso_code;

    @Column(name = "continent", nullable = false, length = 100)
    private String continent;

}
