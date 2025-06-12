package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favourite_hotel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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