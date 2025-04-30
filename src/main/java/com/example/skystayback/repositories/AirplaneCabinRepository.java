package com.example.skystayback.repositories;


import com.example.skystayback.models.AirplaneCabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneCabinRepository extends JpaRepository<AirplaneCabin, Long> {


}