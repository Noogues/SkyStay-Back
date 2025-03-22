package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;


@Data
@Entity
@Table(name = "airplane_image")
public class AirplaneImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private BigInteger id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;


}
