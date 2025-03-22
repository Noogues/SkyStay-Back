package com.example.skystayback.models;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private BigInteger id;

    @Column(name = "url", nullable = false)
    private String url;

}
