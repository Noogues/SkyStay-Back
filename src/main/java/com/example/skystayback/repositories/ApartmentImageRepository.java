package com.example.skystayback.repositories;

import com.example.skystayback.models.ApartmentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApartmentImageRepository extends JpaRepository<ApartmentImage, Long> {


}