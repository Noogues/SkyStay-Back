package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "airline")
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "website", nullable = false)
    private String website;




}
