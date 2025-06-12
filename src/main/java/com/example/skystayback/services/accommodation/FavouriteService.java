package com.example.skystayback.services.accommodation;

import com.example.skystayback.models.*;
import com.example.skystayback.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteHotelRepository favouriteHotelRepository;
    @Autowired
    private FavouriteApartmentRepository favouriteApartmentRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;

    public void addFavourite(String accommodationCode, String type, User user) {
        if ("hotel".equalsIgnoreCase(type)) {
            Hotel hotel = hotelRepository.findByCode(accommodationCode).orElse(null);
            if (hotel != null) {
                favouriteHotelRepository.save(new FavouriteHotel(user, hotel));
            }
        } else {
            Apartment apartment = apartmentRepository.findByCode(accommodationCode).orElse(null);
            if (apartment != null) {
                favouriteApartmentRepository.save(new FavouriteApartment(user, apartment));
            }
        }
    }

    @Transactional
    public void removeFavourite(String accommodationCode, String type, User user) {
        if ("hotel".equalsIgnoreCase(type)) {
            Hotel hotel = hotelRepository.findByCode(accommodationCode).orElse(null);
            if (hotel != null) {
                favouriteHotelRepository.deleteByUserAndHotel(user, hotel);
            }
        } else {
            Apartment apartment = apartmentRepository.findByCode(accommodationCode).orElse(null);
            if (apartment != null) {
                favouriteApartmentRepository.deleteByUserAndApartment(user, apartment);
            }
        }
    }

    public boolean isFavourite(String accommodationCode, String type, User user) {
        if ("hotel".equalsIgnoreCase(type)) {
            Hotel hotel = hotelRepository.findByCode(accommodationCode).orElse(null);
            return hotel != null && favouriteHotelRepository.existsByUserAndHotel(user, hotel);
        } else {
            Apartment apartment = apartmentRepository.findByCode(accommodationCode).orElse(null);
            return apartment != null && favouriteApartmentRepository.existsByUserAndApartment(user, apartment);
        }
    }
}