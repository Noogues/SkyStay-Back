package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.airline.AirlineAddVO;
import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.models.Airline;
import com.example.skystayback.repositories.AirlineRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor

public class AirlineAdministrationService {

    private final AirlineRepository airlineRepository;
    private final UserService userService;
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
}
