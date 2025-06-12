package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favourite_apartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteApartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    public FavouriteApartment(User user, Apartment apartment) {
        this.user = user;
        this.apartment = apartment;
    }
}