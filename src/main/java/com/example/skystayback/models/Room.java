package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Column(name = "state", nullable = false)
    private Boolean state;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_configuration_hotel_id", nullable = false)
    private RoomConfigurationHotel roomConfiguration;
}
