package com.example.skystayback.services.accommodation;

import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.common.AccommodationResponseVO;
import com.example.skystayback.dtos.common.RoomDetailsVO;
import com.example.skystayback.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GlobalService {

    @Autowired
    private HotelRepository hotelRepository;

    public ResponseVO<List<AccommodationResponseVO>> searchAccommodations(
            String destination,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer adults,
            Integer children,
            Integer rooms) {

        List<AccommodationResponseVO> hotels = findByFilters(destination, checkIn, checkOut, adults, children, rooms);

        return ResponseVO.<List<AccommodationResponseVO>>builder()
                .response(new DataVO<>(hotels))
                .messages(new MessageResponseVO("Alojamientos encontrados", 200, LocalDateTime.now()))
                .build();
    }

    private List<AccommodationResponseVO> findByFilters(String destination, LocalDate checkIn, LocalDate checkOut, Integer adults, Integer children, Integer rooms) {
        List<AccommodationResponseVO> hotels = hotelRepository.findHotelsByDestination(destination);
        hotels.removeIf(hotel -> {
            List<RoomDetailsVO> availableRooms = hotelRepository.findAvailableRoomsByHotel(hotel.getHotelId(), checkIn, checkOut, rooms, adults, children);
            hotel.setAvailableRooms(availableRooms);
            return availableRooms.isEmpty(); // Filtra hoteles sin habitaciones disponibles
        });
        return hotels;
    }

    public List<String> getAllCities() {
        return hotelRepository.findAllCities();
    }
}