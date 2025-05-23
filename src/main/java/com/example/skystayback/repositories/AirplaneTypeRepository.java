package com.example.skystayback.repositories;

import com.example.skystayback.dtos.airplanes.AirplaneShowVO;
import com.example.skystayback.models.Airplane;
import com.example.skystayback.models.AirplaneType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirplaneTypeRepository extends JpaRepository<AirplaneType, Long> {

    Optional<Object> findByCode(String code);
}