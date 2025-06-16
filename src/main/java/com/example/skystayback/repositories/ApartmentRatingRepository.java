package com.example.skystayback.repositories;

import com.example.skystayback.models.ApartmentRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRatingRepository extends JpaRepository<ApartmentRating, Long> {
    Page<ApartmentRating> findByApartment_Code(String code, Pageable pageable);
}