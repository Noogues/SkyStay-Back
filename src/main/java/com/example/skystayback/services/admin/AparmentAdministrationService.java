package com.example.skystayback.services.admin;


import com.example.skystayback.dtos.apartments.AddRoomImageVO;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.hotel.*;
import com.example.skystayback.enums.RoomType;
import com.example.skystayback.models.*;
import com.example.skystayback.repositories.*;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AparmentAdministrationService {

    private final CityRepository cityRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ApartmentImageRepository apartmentImageRepository;
    private final RoomConfigurationRepository roomConfigurationRepository;
    private final RoomConfigurationApartmentRepository roomConfigurationApartmentRepository;
    private final ApartmentRepository apartmentRepository;
    private final RoomApartmentRepository roomApartmentRepository;


    public ResponsePaginatedVO<HotelAdminVO> getAllApartments(PageVO pageVO) {
        try {
            Page<HotelAdminVO> hotels = apartmentRepository.findAllApartments(pageVO.toPageable());
            ResponsePaginatedVO<HotelAdminVO> data = new ResponsePaginatedVO<>();
            data.setObjects(hotels.getContent());
            data.setHasNextPage(hotels.hasNext());
            data.setHasPreviousPage(hotels.hasPrevious());
            data.setCurrentPage(hotels.getNumber());
            data.setTotalPages(hotels.getTotalPages());
            data.setMessages(new MessageResponseVO("Hoteles recuperados con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los hoteles", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<String> addApartment(HotelFormVO hotelFormVO) {
        try {
            Apartment apartment = new Apartment();
            apartment.setCode(userService.generateShortUuid());
            apartment.setName(hotelFormVO.getName());
            apartment.setAddress(hotelFormVO.getAddress());
            apartment.setPostal_code(hotelFormVO.getPostalCode());
            apartment.setPhone_number(hotelFormVO.getPhone_number());
            apartment.setEmail(hotelFormVO.getEmail());
            apartment.setWebsite(hotelFormVO.getWebsite());
            apartment.setStars(0);
            apartment.setDescription(hotelFormVO.getDescription());
            City city = cityRepository.findById(hotelFormVO.getCityId()).orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            apartment.setCity(city);
            apartmentRepository.save(apartment);

            for (RoomFormVO form : hotelFormVO.getRooms()) {
                Integer totalRooms = form.getTotal_rooms();
                RoomConfiguration roomConfiguration = new RoomConfiguration();
                roomConfiguration.setCode(userService.generateShortUuid());
                roomConfiguration.setCapacity(form.getCapacity());
                roomConfiguration.setType(RoomType.valueOf(form.getType()));
                roomConfigurationRepository.save(roomConfiguration);
                RoomConfigurationApartment roomConfigurationApartment = new RoomConfigurationApartment();
                roomConfigurationApartment.setApartment(apartment);
                roomConfigurationApartment.setRoomConfiguration(roomConfiguration);
                roomConfigurationApartmentRepository.save(roomConfigurationApartment);
                int cont = 0;
                while (totalRooms > 0) {
                    cont += 1;
                    RoomApartment room = new RoomApartment();
                    room.setRoom_number(cont);
                    room.setState(true);
                    room.setRoomConfiguration(roomConfigurationApartment);
                    roomApartmentRepository.save(room);
                    totalRooms--;
                }
            }
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Hotel creado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear el hotel", 500, LocalDateTime.now()));
        }
    }


    public ResponseVO<Void> addApartmentImage(AddImageVO form) {
        try {
            Apartment apartment = apartmentRepository.findByCode(form.getCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el avión con el código proporcionado"));

            Image image = new Image();
            image.setUrl(form.getImage());
            image = imageRepository.save(image);

            ApartmentImage apartmentImage = new ApartmentImage();
            apartmentImage.setApartment(apartment);
            apartmentImage.setImage(image);
            apartmentImageRepository.save(apartmentImage);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Imagen del hotel añadida con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al añadir la imagen del hotel.", 404, LocalDateTime.now()));
        }
    }


    public ResponseVO<ShowHotelDetails> getApartmentByCode(String apartmentCode) {
        try {
            ShowHotelDetails hotelDetails = apartmentRepository.findApartmentDetailsByCode(apartmentCode);
            hotelDetails.setRoomsDetails(new ArrayList<>());
            List<RoomConfigurationVO> list = roomConfigurationApartmentRepository.findRoomDetailsByApartmentId(hotelDetails.getId());
            for (RoomConfigurationVO rc : list) {
                ShowRoomsDetails room = new ShowRoomsDetails();
                room.setRoomCapacity(rc.getCapacity());
                room.setRoomType(rc.getType());
                room.setImage(rc.getUrl());
                List<RoomVO> roomVOList = roomApartmentRepository.findAllRoomsByApartmentCodeAndRoomConfigurationId(rc.getId());
                // Filtrar en la lista todos los valores nulos
                List<RoomVO> filteredRooms = roomVOList.stream()
                        .filter(r -> r.getRoomNumber() != null && r.getState() != null)
                        .toList();

                if (!filteredRooms.isEmpty()) {
                    room.setRooms(filteredRooms);
                    hotelDetails.getRoomsDetails().add(room);
                }
            }
            System.out.println(hotelDetails);
            return new ResponseVO<>(new DataVO<>(hotelDetails), new MessageResponseVO("Apartamento recuperado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al recuperar el hotel", 500, LocalDateTime.now()));
        }
    }


    public ResponseVO<Void> addRoomImage(AddRoomImageVO form) {
        try {
            Apartment apartment = apartmentRepository.findByCode(form.getApartmentCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el hotel con el código proporcionado"));
            RoomConfigurationApartment roomConfigurationApartment = roomConfigurationApartmentRepository.findByApartmentAndRoomType(apartment.getCode(), RoomType.valueOf(form.getRoomType()));
            roomConfigurationApartment.setUrl(form.getUrl());
            roomConfigurationApartmentRepository.save(roomConfigurationApartment);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Imagen de habitación añadida con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al añadir la imagen de habitación.", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<Void> editApartment(EditHotelVO form){
        try {
            Apartment apartment = apartmentRepository.findByCode(form.getCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el hotel con el código proporcionado"));
            apartment.setPhone_number(form.getPhoneNumber());
            apartment.setEmail(form.getEmail());
            apartment.setWebsite(form.getWebsite());
            apartment.setDescription(form.getDescription());
            apartmentRepository.save(apartment);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Hotel editado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al editar el hotel.", 404, LocalDateTime.now()));
        }
    }
}

