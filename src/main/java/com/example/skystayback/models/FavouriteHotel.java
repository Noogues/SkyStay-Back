package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "favourite_hotel")
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    public FavouriteHotel(User user, Hotel hotel) {
        this.user = user;
        this.hotel = hotel;
    }
}