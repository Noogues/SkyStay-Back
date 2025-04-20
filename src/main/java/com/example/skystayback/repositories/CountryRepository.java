package com.example.skystayback.repositories;


import com.example.skystayback.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByNameAndContinent(String name, String continent);

}