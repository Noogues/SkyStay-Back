package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private BigInteger id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;



}
