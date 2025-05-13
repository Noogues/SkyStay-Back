package com.example.skystayback.repositories;




import com.example.skystayback.models.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {


}