package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "apartment")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, length = 16)
    private String code;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "website", nullable = false, length = 120)
    private String website;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "amenities", length = 500)
    private String amenities;
}
