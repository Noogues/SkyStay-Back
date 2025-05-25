package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.dtos.airports.AirportFormVO;
import com.example.skystayback.dtos.airports.AirportReducedVO;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.models.Airport;
import com.example.skystayback.models.City;
import com.example.skystayback.repositories.AirportRepository;
import com.example.skystayback.repositories.CityRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AirportsAdministrationService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final UserService userService;

    /**
     * Obtiene todos los aeropuertos disponibles en la base de datos.
     * @param pageVO Objeto que contiene la información de paginación.
     * @return Un objeto ResponsePaginatedVO que contiene la lista de aeropuertos y la información de paginación.
     */
    public ResponsePaginatedVO<AirportAdminVO> getAirports(PageVO pageVO) {
        try {
            Page<AirportAdminVO> airports = airportRepository.getAllAirports(pageVO.toPageable());
            ResponsePaginatedVO<AirportAdminVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airports.getContent());
            data.setHasNextPage(airports.hasNext());
            data.setHasPreviousPage(airports.hasPrevious());
            data.setCurrentPage(airports.getNumber());
            data.setTotalPages(airports.getTotalPages());
            data.setMessages(new MessageResponseVO("Aeropuertos recuperados con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAirports: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los aeropuertos:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Crea un nuevo aeropuerto en la base de datos.
     * @param airportAdminVO Objeto que contiene la información del aeropuerto a crear.
     * @return Un objeto ResponseVO que indica el resultado de la operación.
     */
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
            System.out.println("createAirport: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear el aeropuerto", 500, LocalDateTime.now()));
        }
    }

    /**
     * Actualiza un aeropuerto existente en la base de datos.
     * @param code El código del aeropuerto a actualizar.
     * @param airportAdminVO Objeto que contiene la información actualizada del aeropuerto.
     * @return Un objeto ResponseVO que indica el resultado de la operación.
     */
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
            System.out.println("updateAirport: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al actualizar el aeropuerto", 500, LocalDateTime.now()));
        }
    }

    public ResponsePaginatedVO<AirportReducedVO> getAirportsReduced(PageVO pageVO) {
        try {
            Page<AirportReducedVO> airports = airportRepository.getAllAirportsReduced(pageVO.toPageable());
            ResponsePaginatedVO<AirportReducedVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airports.getContent());
            data.setHasNextPage(airports.hasNext());
            data.setHasPreviousPage(airports.hasPrevious());
            data.setCurrentPage(airports.getNumber());
            data.setTotalPages(airports.getTotalPages());
            data.setMessages(new MessageResponseVO("Aeropuertos recuperados con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAirportsReduced: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los aeropuertos:", 404, LocalDateTime.now()));
        }
    }
}
