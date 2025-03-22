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
    private BigInteger id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "last_name", nullable = false, length = 150)
    private String last_name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birth_date;

    @Column(name = "price", nullable = false, scale = 2)
    private Float price;

    @Column(name = "booking_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus booking_status;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id")
    private SeatBooking seat;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "additional_baggage_id")
    private AdditionalBaggage additionalBaggage;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderFlight order;

}
