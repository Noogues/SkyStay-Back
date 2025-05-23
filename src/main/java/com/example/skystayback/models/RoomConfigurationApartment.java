package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room_configuration_apartment")
public class RoomConfigurationApartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_configuration_id", nullable = false)
    private RoomConfiguration roomConfiguration;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @Column(name = "url")
    private String url;
}
