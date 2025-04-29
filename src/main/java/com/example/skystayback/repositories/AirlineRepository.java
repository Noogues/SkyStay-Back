package com.example.skystayback.repositories;


import com.example.skystayback.models.Airline;
import com.example.skystayback.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

}