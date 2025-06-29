package com.example.skystayback.models;

import com.example.skystayback.enums.SeatClass;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "airplane_cabin")
public class AirplaneCabin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @ManyToOne
    @JoinColumn(name = "seat_configuration_id", nullable = false)
    private SeatConfiguration seatConfiguration;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "class", nullable = false)
    private SeatClass seatClass;

    @Column(name = "row_start")
    private Integer rowStart;

    @Column(name = "row_end")
    private Integer rowEnd;
}
