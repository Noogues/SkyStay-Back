package com.example.skystayback.repositories;

import com.example.skystayback.models.RoomConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomConfigurationRepository extends JpaRepository<RoomConfiguration, Long> {


}