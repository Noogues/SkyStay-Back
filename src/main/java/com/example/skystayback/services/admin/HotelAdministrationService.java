package com.example.skystayback.services.admin;

import com.example.skystayback.converter.AdminConverter;
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
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelAdministrationService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final HotelImageRepository hotelImageRepository;
    private final RoomRepository roomRepository;
    private final RoomConfigurationRepository roomConfigurationRepository;
    private final RoomConfigurationHotelRepository roomConfigurationHotelRepository;

    /**
     * Metodo para obtener todos los hoteles
     * @param pageVO informacion de paginacion
     * @return lista de hoteles
     */
    public ResponsePaginatedVO<HotelAdminVO> getAllHotels(PageVO pageVO) {
        try {
            Page<HotelAdminVO> hotels = hotelRepository.findAllHotels(pageVO.toPageable());
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


    /**
     * Metodo para añadir un hotel (con sus diferentes habitaciones)
     * @param hotelFormVO json con los datos del hotel
     * @return mensaje de exito o error
     */
    public ResponseVO<String> addHotel(HotelFormVO hotelFormVO) {
        try {
            Hotel hotel = new Hotel();
            hotel.setCode(userService.generateShortUuid());
            hotel.setName(hotelFormVO.getName());
            hotel.setAddress(hotelFormVO.getAddress());
            hotel.setPostal_code(hotelFormVO.getPostalCode());
            hotel.setPhone_number(hotelFormVO.getPhone_number());
            hotel.setEmail(hotelFormVO.getEmail());
            hotel.setWebsite(hotelFormVO.getWebsite());
            hotel.setStars(0);
            hotel.setDescription(hotelFormVO.getDescription());
            City city = cityRepository.findById(hotelFormVO.getCityId()).orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            hotel.setCity(city);
            hotelRepository.save(hotel);

            for (RoomFormVO form : hotelFormVO.getRooms()) {
                Integer totalRooms = form.getTotal_rooms();
                RoomConfiguration roomConfiguration = new RoomConfiguration();
                roomConfiguration.setCode(userService.generateShortUuid());
                roomConfiguration.setCapacity(form.getCapacity());
                roomConfiguration.setType(RoomType.valueOf(form.getType()));
                roomConfigurationRepository.save(roomConfiguration);
                RoomConfigurationHotel roomConfigurationHotel = new RoomConfigurationHotel();
                roomConfigurationHotel.setHotel(hotel);
                roomConfigurationHotel.setRoomConfiguration(roomConfiguration);
                roomConfigurationHotelRepository.save(roomConfigurationHotel);
                int cont = 0;
                while (totalRooms > 0) {
                    cont += 1;
                    Room room = new Room();
                    room.setRoom_number(cont);
                    room.setState(true);
                    room.setRoomConfiguration(roomConfigurationHotel);
                    roomRepository.save(room);
                    totalRooms--;
                }
            }
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Hotel creado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear el hotel", 500, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para añadir una imagen a un hotel
     * @param form json con los datos de la imagen
     * @return mensaje de exito o error
     */
    public ResponseVO<Void> addHotelImage(AddImageVO form) {
        try {
            Hotel hotel = hotelRepository.findByCode(form.getCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el avión con el código proporcionado"));

            Image image = new Image();
            image.setUrl(form.getImage());
            image = imageRepository.save(image);

            HotelImage hotelImage = new HotelImage();
            hotelImage.setHotel(hotel);
            hotelImage.setImage(image);
            hotelImageRepository.save(hotelImage);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Imagen del hotel añadida con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al añadir la imagen del hotel.", 404, LocalDateTime.now()));
        }
    }


    /**
     * Metodo para obtener todos los tipos de habitaciones
     * @return lista de tipos de habitaciones
     */
    public ResponseVO<List<RoomType>> getAllRoomTypes() {
        try {
            List<RoomType> roomTypes = List.of(RoomType.values());
            return new ResponseVO<>(new DataVO<>(roomTypes), new MessageResponseVO("Tipos de habitaciones recuperados con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(new ArrayList<>()), new MessageResponseVO("Error al recuperar los tipos de habitaciones", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener todas las configuraciones de habitaciones
     * @return lista de configuraciones de habitaciones
     */
    public ResponseVO<List<RoomConfigurationVO>> getAllRoomConfigurations() {
        try {
            List<RoomConfiguration> roomConfigurations = roomConfigurationRepository.findAll();
            List<RoomConfigurationVO> roomConfigurationVOs = roomConfigurations.stream()
                    .map(AdminConverter::convertToRoomConfigurationVO)
                    .toList();
            return new ResponseVO<>(new DataVO<>(roomConfigurationVOs), new MessageResponseVO("Configuraciones de habitaciones recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(new ArrayList<>()), new MessageResponseVO("Error al recuperar las configuraciones de habitaciones", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener los detalles de un hotel mediante su codigo
     * @param hotelCode codigo del hotel
     * @return detalles del hotel
     */
    public ResponseVO<ShowHotelDetails> getHotel(String hotelCode) {
        try {
            ShowHotelDetails hotelDetails = hotelRepository.findHotelDetailsByCode(hotelCode);
            hotelDetails.setRoomsDetails(new ArrayList<>());
            List<RoomConfigurationVO> list = roomConfigurationHotelRepository.findRoomDetailsByHotelId(hotelDetails.getId());
            for (RoomConfigurationVO rc : list) {
                ShowRoomsDetails room = new ShowRoomsDetails();
                room.setRoomCapacity(rc.getCapacity());
                room.setRoomType(rc.getType());
                room.setImage(rc.getUrl());
                List<RoomVO> roomVOList = roomRepository.findAllRoomsByHotelCodeAndRoomConfigurationId(rc.getId());
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
            return new ResponseVO<>(new DataVO<>(hotelDetails), new MessageResponseVO("Hotel recuperado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al recuperar el hotel", 500, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para añadir una imagen a una habitacion
     * @param form json con los datos de la imagen
     * @return mensaje de exito o error
     */
    public ResponseVO<Void> addRoomImage(AddRoomImageVO form) {
        try {
            Hotel hotel = hotelRepository.findByCode(form.getHotelCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el hotel con el código proporcionado"));
            RoomConfigurationHotel roomConfigurationHotel = roomConfigurationHotelRepository.findByHotelAndRoomType(hotel.getCode(), RoomType.valueOf(form.getRoomType()));
            roomConfigurationHotel.setUrl(form.getUrl());
            roomConfigurationHotelRepository.save(roomConfigurationHotel);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Imagen de habitación añadida con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al añadir la imagen de habitación.", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para editar un hotel
     * @param form json con los datos del hotel
     * @return mensaje de exito o error
     */
    public ResponseVO<Void> editHotel (EditHotelVO form){
        try {
            Hotel hotel = hotelRepository.findByCode(form.getCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el hotel con el código proporcionado"));
            hotel.setPhone_number(form.getPhoneNumber());
            hotel.setEmail(form.getEmail());
            hotel.setWebsite(form.getWebsite());
            hotel.setDescription(form.getDescription());
            hotelRepository.save(hotel);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Hotel editado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al editar el hotel.", 404, LocalDateTime.now()));
        }
    }
}

