package com.example.skystayback.services.accommodation;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<DestinationVO> getTopDestinations() {
        Pageable limit = PageRequest.of(0, 3);
        List<DestinationVO> hotels = hotelRepository.findRandomHotels(limit);
        List<DestinationVO> apartments = hotelRepository.findRandomApartments(limit);

        List<DestinationVO> combined = new ArrayList<>();
        combined.addAll(hotels);
        combined.addAll(apartments);

        if (combined.size() < 6) {
            if (hotels.size() < 3) {
                combined.addAll(hotelRepository.findRandomHotels(PageRequest.of(0, 3 - combined.size())));
            }
            if (apartments.size() < 3 && combined.size() < 3) {
                combined.addAll(hotelRepository.findRandomApartments(PageRequest.of(0, 3 - combined.size())));
            }
        }

        return combined.stream().limit(6).collect(Collectors.toList());
    }
}