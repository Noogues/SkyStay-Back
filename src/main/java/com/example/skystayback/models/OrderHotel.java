package com.example.skystayback.models;

import com.example.skystayback.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "order_hotel")
public class OrderHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private BigInteger id;

    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @Column(name = "amount", nullable = false, scale = 2)
    private Float amount;

    @Column(name = "discount", nullable = false, scale = 2)
    private Float discount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StatusOrder status;

    @Column(columnDefinition = "LONGBLOB", name = "bill")
    private byte[] bill;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

}
