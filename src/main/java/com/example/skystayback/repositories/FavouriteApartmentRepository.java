package com.example.skystayback.repositories;

import com.example.skystayback.models.Apartment;
import com.example.skystayback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.skystayback.models.FavouriteApartment;

public interface FavouriteApartmentRepository extends JpaRepository<FavouriteApartment, Long> {
    void deleteByUserAndApartment(User user, Apartment apartment);
    boolean existsByUserAndApartment(User user, Apartment apartment);
}