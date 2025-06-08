package com.example.skystayback.models;

import com.example.skystayback.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_ticket")
public class UserTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, length = 16, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "booking_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "additional_baggage_id")
    private AdditionalBaggage additionalBaggage;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderFlight order;

}
