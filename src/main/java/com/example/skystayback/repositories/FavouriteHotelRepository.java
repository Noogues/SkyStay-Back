package com.example.skystayback.repositories;

import com.example.skystayback.models.FavouriteHotel;
import com.example.skystayback.models.Hotel;
import com.example.skystayback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteHotelRepository extends JpaRepository<FavouriteHotel, Long> {
    void deleteByUserAndHotel(User user, Hotel hotel);
    boolean existsByUserAndHotel(User user, Hotel hotel);
}
