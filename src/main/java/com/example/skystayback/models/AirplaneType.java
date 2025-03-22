package com.example.skystayback.models;

import com.example.skystayback.enums.AirplaneConfiguration;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "airplane_type")
public class AirplaneType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private BigInteger id;


    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "configuration", nullable = false, length = 100)
    @Enumerated(EnumType.ORDINAL)
    private AirplaneConfiguration configuration;

}
