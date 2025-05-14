package com.example.skystayback.models;
import com.example.skystayback.enums.SeatClass;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seat_configuration")
public class SeatConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_pattern" , nullable = false)
    private String seatPattern;

    @Column(name = "description", nullable = false)
    private String description;
}

