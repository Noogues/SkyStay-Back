package com.example.skystayback.repositories;


import com.example.skystayback.models.Image;
import com.example.skystayback.models.OrderApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderApartmentRepository extends JpaRepository<OrderApartment, Long> {

}