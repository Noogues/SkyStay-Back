package com.example.skystayback.repositories;

import com.example.skystayback.dtos.city.CityTableVO;
import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.models.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);

    @Query("SELECT new com.example.skystayback.dtos.city.CityVO(c.name, new com.example.skystayback.dtos.city.CountryVO(co.name)) " +
            "FROM City c INNER JOIN Country co ON c.country.id = co.id")
    Page<CityVO> getAllCities(Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.city.CityTableVO(c.id, c.name) FROM City c")
    Page<CityTableVO> getAllCitiesTable(Pageable pageable);
}