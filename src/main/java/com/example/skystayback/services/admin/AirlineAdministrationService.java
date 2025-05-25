package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.airline.*;
import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.models.Airline;
import com.example.skystayback.repositories.AirlineRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor

public class AirlineAdministrationService {

    private final AirlineRepository airlineRepository;
    private final UserService userService;

    public ResponsePaginatedVO<AirlineTableVO> getAllAirlines(PageVO pageVO) {
        try {
            Page<AirlineTableVO> airlines = airlineRepository.getAllAirlines(pageVO.toPageable());
            ResponsePaginatedVO<AirlineTableVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airlines.getContent());
            data.setHasNextPage(airlines.hasNext());
            data.setHasPreviousPage(airlines.hasPrevious());
            data.setCurrentPage(airlines.getNumber());
            data.setTotalPages(airlines.getTotalPages());
            data.setMessages(new MessageResponseVO("Aerolineas recuperadas con exito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllAirlines" + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las aerolineas:", 404, LocalDateTime.now()));
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
            airline.setPhone(airlineAddVO.getPhone());
            airline.setEmail(airlineAddVO.getEmail());
            airline.setWebsite(airlineAddVO.getWebsite());
            airline.setIataCode(airlineAddVO.getIataCode());
            airlineRepository.save(airline);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Aerolinea creada con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("addAirline:" + e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al crear la aerolinea", 500, LocalDateTime.now()));
        }
    }

    public ResponseVO<Void> addImageToAirline(AirlineAddImageVO form) {
        try {
            Airline airline = airlineRepository.findByCode(form.getCode()).orElseThrow(() -> new RuntimeException("Aerolinea no encontrada"));
            airline.setImage(form.getUrl());
            airlineRepository.save(airline);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Imagen de aerolinea actualizada con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al actualizar la imagen de la aerolinea", 500, LocalDateTime.now()));
        }
    }

    public ResponsePaginatedVO<AirlineReducedVO> getAllAirlinesReduced(PageVO pageVO) {
        try {
            Page<AirlineReducedVO> airlines = airlineRepository.getAllAirlinesReduced(pageVO.toPageable());
            ResponsePaginatedVO<AirlineReducedVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airlines.getContent());
            data.setHasNextPage(airlines.hasNext());
            data.setHasPreviousPage(airlines.hasPrevious());
            data.setCurrentPage(airlines.getNumber());
            data.setTotalPages(airlines.getTotalPages());
            data.setMessages(new MessageResponseVO("Aerolineas recuperadas con exito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllAirlinesReduced: " +e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las aerolineas:", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<Void> editAirline(AirlineEditVO form) {
        try {
            Airline airline = airlineRepository.findByCode(form.getCode()).orElseThrow(() -> new RuntimeException("Aerolinea no encontrada"));
            if (form.getName() != null && !form.getName().isEmpty()) airline.setName(form.getName());
            if (form.getPhone() != null && !form.getPhone().isEmpty()) airline.setPhone(form.getPhone());
            if (form.getEmail() != null && !form.getEmail().isEmpty()) airline.setEmail(form.getEmail());
            if (form.getWebsite() != null && !form.getWebsite().isEmpty()) airline.setWebsite(form.getWebsite());
            if (form.getIataCode() != null && !form.getIataCode().isEmpty()) airline.setIataCode(form.getIataCode());
            airlineRepository.save(airline);
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Aerolinea editada con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("editAirline: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Error al editar la aerolinea", 500, LocalDateTime.now()));
        }
    }
}
