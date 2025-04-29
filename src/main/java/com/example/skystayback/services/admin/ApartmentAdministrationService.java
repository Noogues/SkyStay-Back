package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.hotel.HotelApartmentsAddVO;
import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.models.Apartment;
import com.example.skystayback.models.City;
import com.example.skystayback.repositories.ApartmentRepository;
import com.example.skystayback.repositories.CityRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ApartmentAdministrationService {

    private final ApartmentRepository apartmentRepository;
    private final CityRepository cityRepository;
    private final UserService userService;

    /**
     * Metodo para crear un apartamento
     * @param hotelApartmentsAddVO json con los datos del apartamento
     * @return mensaje de exito o error
     */
    public ResponseVO<String> addApartment(HotelApartmentsAddVO hotelApartmentsAddVO){
        try {
            Apartment apartment = new Apartment();
            apartment.setCode(userService.generateShortUuid());
            apartment.setName(hotelApartmentsAddVO.getName());
            apartment.setAddress(hotelApartmentsAddVO.getAddress());
            apartment.setPostal_code(hotelApartmentsAddVO.getPostalCode());
            apartment.setPhone_number(hotelApartmentsAddVO.getPhone_number());
            apartment.setEmail(hotelApartmentsAddVO.getEmail());
            apartment.setWebsite(hotelApartmentsAddVO.getWebsite());
            apartment.setStars(0);
            apartment.setDescription(hotelApartmentsAddVO.getDescription());
            apartment.setAvailability(true);
            City city = cityRepository.findById(hotelApartmentsAddVO.getCityId())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            apartment.setCity(city);
            apartmentRepository.save(apartment);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Apartamento creado con éxito", 200, LocalDateTime.now()));
        }catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear el apartamento", 500, LocalDateTime.now()));
        }
    }
}
