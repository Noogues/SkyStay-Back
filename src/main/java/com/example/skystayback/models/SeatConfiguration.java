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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "class", nullable = false)
    private SeatClass seatClass;

    @Column(name = "seat_pattern" , nullable = false)
    private String seatPattern;

    @Column(name = "total_rows", nullable = false)
    private Integer totalRows;

    @Column(name = "description", nullable = false)
    private String description;
}

