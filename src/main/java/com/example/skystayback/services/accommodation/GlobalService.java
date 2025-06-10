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
            Integer rooms,
            String type) {

        List<AccommodationResponseVO> hotels = findByFilters(destination, checkIn, checkOut, adults, children, rooms, type);

        return ResponseVO.<List<AccommodationResponseVO>>builder()
                .response(new DataVO<>(hotels))
                .messages(new MessageResponseVO("Alojamientos encontrados", 200, LocalDateTime.now()))
                .build();
    }

    private List<AccommodationResponseVO> findByFilters(
            String destination,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer adults,
            Integer children,
            Integer rooms,
            String type) {

        List<AccommodationResponseVO> accommodations = new ArrayList<>();

        // Si no se especifica tipo o se solicitan hoteles, buscar hoteles
        if (type == null || "hotel".equalsIgnoreCase(type)) {
            List<AccommodationResponseVO> hotels = hotelRepository.findHotelsByDestination(destination);
            for (AccommodationResponseVO hotel : hotels) {
                hotel.setAccommodationType("hotel"); // Establecer el tipo
                List<RoomDetailsVO> availableRooms = hotelRepository.findAvailableRoomsByHotel(
                        hotel.getId(), checkIn, checkOut, rooms, adults, children);
                hotel.setAvailableRooms(availableRooms);
                if (!availableRooms.isEmpty()) {
                    accommodations.add(hotel);
                }
            }
        }

        // Si no se especifica tipo o se solicitan apartamentos, buscar apartamentos
        if (type == null || "apartment".equalsIgnoreCase(type)) {
            List<AccommodationResponseVO> apartments = hotelRepository.findApartmentsByDestination(destination);
            for (AccommodationResponseVO apartment : apartments) {
                apartment.setAccommodationType("apartment"); // Establecer el tipo
                List<RoomDetailsVO> availableRooms = hotelRepository.findAvailableRoomsByApartment(
                        apartment.getId(), checkIn, checkOut, rooms, adults, children);
                apartment.setAvailableRooms(availableRooms);
                if (!availableRooms.isEmpty()) {
                    accommodations.add(apartment);
                }
            }
        }

        return accommodations;
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

    public List<DestinationVO> getTopRatedDestinations() {
        Pageable limit = PageRequest.of(0, 3);
        List<DestinationVO> topHotels = hotelRepository.findTopRatedHotels(limit);
        List<DestinationVO> topApartments = hotelRepository.findTopRatedApartments(limit);

        List<DestinationVO> combined = new ArrayList<>();
        combined.addAll(topHotels);
        combined.addAll(topApartments);

        if (combined.size() < 6) {
            if (topHotels.size() < 3) {
                combined.addAll(hotelRepository.findTopRatedHotels(PageRequest.of(0, 3 - topHotels.size())));
            }
            if (topApartments.size() < 3 && combined.size() < 6) {
                combined.addAll(hotelRepository.findTopRatedApartments(PageRequest.of(0, 6 - combined.size())));
            }
        }

        return combined.stream().limit(6).collect(Collectors.toList());
    }



    public ResponseVO<AccommodationDetailVO> getAccommodationDetail(
            Long id,
            String typeAccomodation,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer adults,
            Integer children,
            Integer rooms) {

        AccommodationDetailVO accommodationDetail = null;
        List<String> images;
        List<RoomDetailsVO> availableRooms;

        // Parámetros efectivos
        LocalDate effectiveCheckIn = checkIn;
        LocalDate effectiveCheckOut = checkOut;
        Integer effectiveAdults = adults != null ? adults : 1;
        Integer effectiveChildren = children != null ? children : 0;
        Integer effectiveRooms = rooms != null ? rooms : 1;

        if ("apartment".equalsIgnoreCase(typeAccomodation)) {
            accommodationDetail = hotelRepository.findApartmentDetailById(id);
            if (accommodationDetail == null) {
                return ResponseVO.<AccommodationDetailVO>builder()
                        .response(null)
                        .messages(new MessageResponseVO("No se encontró el alojamiento", 404, LocalDateTime.now()))
                        .build();
            }
            images = hotelRepository.findAllApartmentImages(id);
            availableRooms = hotelRepository.findAvailableRoomsByApartment(
                    id, effectiveCheckIn, effectiveCheckOut, effectiveRooms, effectiveAdults, effectiveChildren);
            accommodationDetail.setAccommodationType("apartment");
        } else { // Por defecto, hotel
            accommodationDetail = hotelRepository.findHotelDetailById(id);
            if (accommodationDetail == null) {
                return ResponseVO.<AccommodationDetailVO>builder()
                        .response(null)
                        .messages(new MessageResponseVO("No se encontró el alojamiento", 404, LocalDateTime.now()))
                        .build();
            }
            images = hotelRepository.findAllHotelImages(id);
            availableRooms = hotelRepository.findAvailableRoomsByHotel(
                    id, effectiveCheckIn, effectiveCheckOut, effectiveRooms, effectiveAdults, effectiveChildren);
            accommodationDetail.setAccommodationType("hotel");
        }

        accommodationDetail.setImages(images);
        accommodationDetail.setAvailableRooms(availableRooms);

        return ResponseVO.<AccommodationDetailVO>builder()
                .response(new DataVO<>(accommodationDetail))
                .messages(new MessageResponseVO("Detalles del alojamiento obtenidos correctamente", 200, LocalDateTime.now()))
                .build();
    }
}