package com.example.skystayback.repositories;

import com.example.skystayback.models.HotelRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRatingRepository extends JpaRepository<HotelRating, Long> {
    Page<HotelRating> findByHotel_Code(String code, Pageable pageable);
}