package com.example.skystayback.models;

import com.example.skystayback.enums.RoomType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room_configuration")
public class RoomConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, length = 16, unique = true)
    private String code;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "room_type", nullable = false)
    private RoomType type;
}
