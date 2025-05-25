package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "additional_baggage")
public class AdditionalBaggage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "extra_amount", nullable = false, scale = 2)
    private Float extraAmount;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id")
    private Airline airline;


}
