package com.example.skystayback.models;

import com.example.skystayback.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "order_flight")
public class OrderFlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, length = 16, unique = true)
    private String code;

    @Column(name = "amount", nullable = false, scale = 2)
    private Double amount;

    @Column(name = "discount", nullable = false, scale = 2)
    private Double discount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StatusOrder status;

    @Column(columnDefinition = "LONGBLOB", name = "bill")
    private byte[] bill;

}
