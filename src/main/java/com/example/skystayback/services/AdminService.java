package com.example.skystayback.services;


import com.example.skystayback.converter.AdminConverter;
import com.example.skystayback.dtos.admin.add.AirlineAddVO;
import com.example.skystayback.dtos.admin.add.AirportFormVO;
import com.example.skystayback.dtos.admin.add.CityAddVO;
import com.example.skystayback.dtos.admin.add.HotelApartmentsAddVO;
import com.example.skystayback.dtos.admin.all.*;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.models.*;
import com.example.skystayback.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final CityRepository cityRepository;
    private final UserService userService;
    private final HotelRepository hotelRepository;
    private final ApartmentRepository apartmentRepository;
    private final AirlineRepository airlineRepository;
    private final CountryRepository countryRepository;
    private final ImageRepository imageRepository;
    private final CityImageRepository cityImageRepository;
    private final AirportRepository airportRepository;

    /**
     * Metodo para obtener todos los usuarios
     * @param pageVO informacion de paginacion
     * @return lista de usuarios
     */
    public ResponsePaginatedVO<UserAdminVO> getUsers(PageVO pageVO) {
        try {
            Page<UserAdminVO> usersPage = adminRepository.getAllUsers(pageVO.toPageable());
            ResponsePaginatedVO<UserAdminVO> data = new ResponsePaginatedVO<>();
            data.setObjects(usersPage.getContent());
            data.setHasNextPage(usersPage.hasNext());
            data.setHasPreviousPage(usersPage.hasPrevious());
            data.setCurrentPage(usersPage.getNumber());
            data.setTotalPages(usersPage.getTotalPages());
            data.setMessages(new MessageResponseVO("Usuarios recuperados con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los usuarios", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las calificaciones de aerolíneas mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de calificaciones de aerolíneas
     */
    public ResponsePaginatedVO<AirlineRatingVO> getAirlaneRating(String userCode, PageVO pageVO) {
        try {
            Page<AirlineRatingVO> airlineRating = adminRepository.findAllAirlineRatingByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<AirlineRatingVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airlineRating.getContent());
            data.setHasNextPage(airlineRating.hasNext());
            data.setHasPreviousPage(airlineRating.hasPrevious());
            data.setCurrentPage(airlineRating.getNumber());
            data.setTotalPages(airlineRating.getTotalPages());
            data.setMessages(new MessageResponseVO("Calificaciones de aerolíneas recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las calificaciones de aerolíneas:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las calificaciones de apartamentos mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de calificaciones de apartamentos
     */
    public ResponsePaginatedVO<ApartmentRatingVO> getApartmentRating(String userCode, PageVO pageVO) {
        try {
            Page<ApartmentRatingVO> apartmentRating = adminRepository.findAllApartmentRatingByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<ApartmentRatingVO> data = new ResponsePaginatedVO<>();
            data.setObjects(apartmentRating.getContent());
            data.setHasNextPage(apartmentRating.hasNext());
            data.setHasPreviousPage(apartmentRating.hasPrevious());
            data.setCurrentPage(apartmentRating.getNumber());
            data.setTotalPages(apartmentRating.getTotalPages());
            data.setMessages(new MessageResponseVO("Calificaciones de apartamentos recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las calificaciones de apartamentos:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las calificaciones de hoteles mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de calificaciones de hoteles
     */
    public ResponsePaginatedVO<HotelRatingVO> getHotelRating(String userCode, PageVO pageVO) {
        try {
            Page<HotelRatingVO> hotelRating = adminRepository.findAllHotelRatingByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<HotelRatingVO> data = new ResponsePaginatedVO<>();
            data.setObjects(hotelRating.getContent());
            data.setHasNextPage(hotelRating.hasNext());
            data.setHasPreviousPage(hotelRating.hasPrevious());
            data.setCurrentPage(hotelRating.getNumber());
            data.setTotalPages(hotelRating.getTotalPages());
            data.setMessages(new MessageResponseVO("Calificaciones de hoteles recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las calificaciones de hoteles:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las ordenes de apartamentos mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de ordenes de apartamentos
     */
    public ResponsePaginatedVO<OrderApartmentVO> getOrderApartment(String userCode, PageVO pageVO) {
        try {
            Page<OrderApartmentVO> orderApartment = adminRepository.findAllOrderApartmentByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<OrderApartmentVO> data = new ResponsePaginatedVO<>();
            data.setObjects(orderApartment.getContent());
            data.setHasNextPage(orderApartment.hasNext());
            data.setHasPreviousPage(orderApartment.hasPrevious());
            data.setCurrentPage(orderApartment.getNumber());
            data.setTotalPages(orderApartment.getTotalPages());
            data.setMessages(new MessageResponseVO("Órdenes de apartamentos recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las órdenes de apartamentos:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las ordenes de vuelos mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de ordenes de vuelos
     */
    public ResponsePaginatedVO<OrderFlightVO> getOrderFlight(String userCode, PageVO pageVO) {
        try {
            Page<OrderFlightVO> orderFlight = adminRepository.findAllOrderFlightByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<OrderFlightVO> data = new ResponsePaginatedVO<>();
            data.setObjects(orderFlight.getContent());
            data.setHasNextPage(orderFlight.hasNext());
            data.setHasPreviousPage(orderFlight.hasPrevious());
            data.setCurrentPage(orderFlight.getNumber());
            data.setTotalPages(orderFlight.getTotalPages());
            data.setMessages(new MessageResponseVO("Órdenes de vuelos recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las órdenes de vuelos:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las ordenes de hoteles mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de ordenes de hoteles
     */
    public ResponsePaginatedVO<OrderHotelVO> getOrderHotel(String userCode, PageVO pageVO) {
        try {
            Page<OrderHotelVO> orderHotel = adminRepository.findAllOrderHotelByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<OrderHotelVO> data = new ResponsePaginatedVO<>();
            data.setObjects(orderHotel.getContent());
            data.setHasNextPage(orderHotel.hasNext());
            data.setHasPreviousPage(orderHotel.hasPrevious());
            data.setCurrentPage(orderHotel.getNumber());
            data.setTotalPages(orderHotel.getTotalPages());
            data.setMessages(new MessageResponseVO("Órdenes de hoteles recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las órdenes de hoteles:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las habitaciones de un hotel mediante su codigo
     * @param hotelCode codigo del hotel
     * @param pageVO informacion de paginacion
     * @return lista de habitaciones del hotel
     */
    public ResponseVO<List<RoomAdminVO>> getRoomsByHotelCode(String hotelCode, PageVO pageVO) {
        try {
            Page<RoomAdminVO> rooms = adminRepository.findAllRoomsByHotelCode(hotelCode, pageVO.toPageable());
            return AdminConverter.convertPageToResponseVO(rooms, "Habitaciones recuperadas con éxito");
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(new ArrayList<>()), new MessageResponseVO("Error al recuperar las habitaciones:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Agrega una lista de ciudades y sus países asociados a la base de datos.
     * Si un país ya existe con los mismos datos, no se crea uno nuevo.
     *
     * @param cityCountryVOList Lista de objetos `CityAddVO` que contienen los datos de las ciudades y sus países.
     *                          Cada objeto incluye información sobre el nombre de la ciudad, descripción,
     *                          población, país y URL de la imagen.
     * @return Un objeto `ResponseVO<String>` que contiene un mensaje indicando el resultado de la operación.
     */

    public ResponseVO<String> addCityCountry(List<CityAddVO> cityCountryVOList) {
        try {
            for (CityAddVO cityAddVO : cityCountryVOList) {
                Country country = countryRepository.findByNameAndContinent(
                        cityAddVO.getCountry().getName(),
                        cityAddVO.getCountry().getContinent()
                ).orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(cityAddVO.getCountry().getName());
                    newCountry.setIso_code(cityAddVO.getCountry().getIso_code());
                    newCountry.setContinent(cityAddVO.getCountry().getContinent());
                    return countryRepository.save(newCountry);
                });

                City city = new City();
                city.setName(cityAddVO.getName());
                city.setDescription(cityAddVO.getDescription());
                city.setPopulation(cityAddVO.getPopulation());
                city.setCountry(country);
                city = cityRepository.save(city);

                Image image = new Image();
                image.setUrl(cityAddVO.getImage());
                image = imageRepository.save(image);

                CityImage cityImage = new CityImage();
                cityImage.setCity(city);
                cityImage.setImage(image);
                cityImageRepository.save(cityImage);
            }

            return ResponseVO.<String>builder()
                    .messages(new MessageResponseVO("Paises y ciudades añadidas con exito", 200, LocalDateTime.now()))
                    .build();
        } catch (Exception e) {
            return ResponseVO.<String>builder()
                    .messages(new MessageResponseVO("Error al añadir paises y ciudades: " + e.getMessage(), 500, LocalDateTime.now()))
                    .build();
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

    /**
     * Metodo para crear una aerolinea
     * @param airlineAddVO json con los datos de la aerolinea
     * @return mensaje de exito o error
     */
    public ResponseVO<String> addAirline(AirlineAddVO airlineAddVO) {
        try {
            Airline airline = new Airline();
            airline.setCode(userService.generateShortUuid());
            airline.setName(airlineAddVO.getName());
            airline.setImage(airlineAddVO.getImage());
            airline.setPhone(airlineAddVO.getPhone());
            airline.setEmail(airlineAddVO.getEmail());
            airline.setWebsite(airlineAddVO.getWebsite());
            airlineRepository.save(airline);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Aerolinea creado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear la aerolinea", 500, LocalDateTime.now()));
        }
    }

    public ResponsePaginatedVO<AirportAdminVO> getAirports(PageVO pageVO) {
        try {
            Page<AirportAdminVO> airports = adminRepository.getAllAirports(pageVO.toPageable());
            ResponsePaginatedVO<AirportAdminVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airports.getContent());
            data.setHasNextPage(airports.hasNext());
            data.setHasPreviousPage(airports.hasPrevious());
            data.setCurrentPage(airports.getNumber());
            data.setTotalPages(airports.getTotalPages());
            data.setMessages(new MessageResponseVO("Aeropuertos recuperados con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los aeropuertos:", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO <List<CityVO>> getCities() {
        try {
            List<CityVO> cities = adminRepository.getAllCities();
            return new ResponseVO<>(new DataVO<>(cities), new MessageResponseVO("Ciudades recuperadas con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(new ArrayList<>()), new MessageResponseVO("Error al recuperar las ciudades:", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<String> createAirport(AirportFormVO airportAdminVO) {
        try {
            Airport airport = new Airport();
            airport.setCode(userService.generateShortUuid());
            airport.setIataCode(airportAdminVO.getIataCode());
            airport.setName(airportAdminVO.getName());
            airport.setDescription(airportAdminVO.getDescription());
            airport.setTerminal(airportAdminVO.getTerminal());
            airport.setGate(airportAdminVO.getGate());
            airport.setLatitude(airportAdminVO.getLatitude());
            airport.setLongitude(airportAdminVO.getLongitude());
            airport.setTimezone(airportAdminVO.getTimezone());
            City city = cityRepository.findByName(airportAdminVO.getCity()).orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            airport.setCity(city);
            airportRepository.save(airport);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Aeropuerto creado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear el aeropuerto", 500, LocalDateTime.now()));
        }
    }

    public ResponseVO<String> updateAirport(String code, AirportFormVO airportAdminVO) {
        try {
            Airport airport = airportRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Aeropuerto no encontrado"));
            if (!airport.getName().equals(airportAdminVO.getName())) {
                airport.setName(airportAdminVO.getName());
            }
            if (!airport.getDescription().equals(airportAdminVO.getDescription())) {
                airport.setDescription(airportAdminVO.getDescription());
            }
            if (!airport.getTerminal().equals(airportAdminVO.getTerminal())) {
                airport.setTerminal(airportAdminVO.getTerminal());
            }
            if (!airport.getGate().equals(airportAdminVO.getGate())) {
                airport.setGate(airportAdminVO.getGate());
            }
            if (airport.getLatitude() == null || airport.getLatitude().compareTo(airportAdminVO.getLatitude()) != 0) {
                airport.setLatitude(airportAdminVO.getLatitude());
            }
            if (airport.getLongitude() == null || airport.getLongitude().compareTo(airportAdminVO.getLongitude()) != 0) {
                airport.setLongitude(airportAdminVO.getLongitude());
            }
            if (!airport.getTimezone().equals(airportAdminVO.getTimezone())) {
                airport.setTimezone(airportAdminVO.getTimezone());
            }
            City city = cityRepository.findByName(airportAdminVO.getCity()).orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            if (!airport.getCity().equals(city)) {
                airport.setCity(city);
            }
            airportRepository.save(airport);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Aeropuerto actualizado con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al actualizar el aeropuerto", 500, LocalDateTime.now()));
        }
    }
}
