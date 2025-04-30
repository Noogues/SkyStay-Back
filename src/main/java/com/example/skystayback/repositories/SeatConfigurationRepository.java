package com.example.skystayback.repositories;


import com.example.skystayback.dtos.airplanes.SeatConfigurationVO;
import com.example.skystayback.models.SeatConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SeatConfigurationRepository extends JpaRepository<SeatConfiguration, Long> {

}