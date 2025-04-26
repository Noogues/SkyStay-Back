package com.example.skystayback.repositories;


import com.example.skystayback.models.AirplaneImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AirplaneImageRepository extends JpaRepository<AirplaneImage, Long> {

}