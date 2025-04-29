package com.example.skystayback.services.admin;

import com.example.skystayback.converter.AdminConverter;
import com.example.skystayback.dtos.hotel.HotelApartmentsAddVO;
import com.example.skystayback.dtos.hotel.RoomAdminVO;
import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.models.City;
import com.example.skystayback.models.Hotel;
import com.example.skystayback.repositories.CityRepository;
import com.example.skystayback.repositories.HotelRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelAdministrationService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final UserService userService;

    /**
     * Metodo para obtener las habitaciones de un hotel mediante su codigo
     * @param hotelCode codigo del hotel
     * @param pageVO informacion de paginacion
     * @return lista de habitaciones del hotel
     */
    public ResponseVO<List<RoomAdminVO>> getRoomsByHotelCode(String hotelCode, PageVO pageVO) {
        try {
            Page<RoomAdminVO> rooms = hotelRepository.findAllRoomsByHotelCode(hotelCode, pageVO.toPageable());
            return AdminConverter.convertPageToResponseVO(rooms, "Habitaciones recuperadas con éxito");
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(new ArrayList<>()), new MessageResponseVO("Error al recuperar las habitaciones:", 404, LocalDateTime.now()));
        }
    }



    /**
     * Metodo para crear un hotel
     * @param hotelApartmentsAddVO json con los datos del hotel
     * @return mensaje de exito o error
     */
    public ResponseVO<String> addHotel(HotelApartmentsAddVO hotelApartmentsAddVO) {
        try {
            Hotel hotel = new Hotel();
            hotel.setCode(userService.generateShortUuid());
            hotel.setName(hotelApartmentsAddVO.getName());
            hotel.setAddress(hotelApartmentsAddVO.getAddress());
            hotel.setPostal_code(hotelApartmentsAddVO.getPostalCode());
            hotel.setPhone_number(hotelApartmentsAddVO.getPhone_number());
            hotel.setEmail(hotelApartmentsAddVO.getEmail());
            hotel.setWebsite(hotelApartmentsAddVO.getWebsite());
            hotel.setStars(0);
            hotel.setDescription(hotelApartmentsAddVO.getDescription());
            City city = cityRepository.findById(hotelApartmentsAddVO.getCityId())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            hotel.setCity(city);
            hotelRepository.save(hotel);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Hotel creado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear el hotel", 500, LocalDateTime.now()));
        }
    }
}
