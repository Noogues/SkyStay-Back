package com.example.skystayback.repositories;


import com.example.skystayback.dtos.user.UserAdminVO;
import com.example.skystayback.models.OrderApartment;
import com.example.skystayback.models.OrderHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderHotelRepository extends JpaRepository<OrderHotel, Long> {

}