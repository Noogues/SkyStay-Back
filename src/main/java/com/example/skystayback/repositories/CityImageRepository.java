package com.example.skystayback.repositories;

import com.example.skystayback.models.CityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CityImageRepository extends JpaRepository<CityImage, Long> {

}