package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageReducedVO;
import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageTableVO;
import com.example.skystayback.dtos.additionalBaggage.AdditionalBaggageVO;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.models.AdditionalBaggage;
import com.example.skystayback.models.Airline;
import com.example.skystayback.repositories.AdditionalBaggageRepository;
import com.example.skystayback.repositories.AirlineRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AdditionalBaggageAdministrationService {

    private final AdditionalBaggageRepository additionalBaggageRepository;
    private final AirlineRepository airlineRepository;

    /**
     * Obtiene todos los equipamientos adicionales disponibles en la base de datos.
     * @param pageVO Objeto que contiene la información de paginación.
     * @return Un objeto ResponsePaginatedVO que contiene la lista de equipamientos adicionales y la información de paginación.
     */
    public ResponsePaginatedVO<AdditionalBaggageTableVO> getAllAdditionalBaggages(PageVO pageVO) {
        try {
            Page<AdditionalBaggageTableVO> additionalBaggages = additionalBaggageRepository.getAllAdditionalBaggage(pageVO.toPageable());
            ResponsePaginatedVO<AdditionalBaggageTableVO> data = new ResponsePaginatedVO<>();
            data.setObjects(additionalBaggages.getContent());
            data.setHasNextPage(additionalBaggages.hasNext());
            data.setHasPreviousPage(additionalBaggages.hasPrevious());
            data.setCurrentPage(additionalBaggages.getNumber());
            data.setTotalPages(additionalBaggages.getTotalPages());
            data.setMessages(new MessageResponseVO("Se ha podido obtener todos los equipamientos.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllAdditionalBaggages: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Ha ocurrido un error a la hora de obtener todos los equipamientos", 404, LocalDateTime.now()));
        }
    }

    /**
     * Crea un nuevo equipamiento adicional en la base de datos.
     * @param form Objeto que contiene la información del equipamiento adicional a crear.
     * @return Un objeto ResponseVO que indica el resultado de la operación.
     */
    public ResponseVO<Void> createAdditionalBaggage(AdditionalBaggageVO form) {
        try {
            AdditionalBaggage object = new AdditionalBaggage();
            object.setName(form.getName());
            object.setWeight(form.getWeight());
            object.setExtraAmount(form.getExtraAmount());
            Airline airline = airlineRepository.findById(form.getAirline_id()).orElseThrow(() -> new RuntimeException("Aerolinea no encontrada"));
            object.setAirline(airline);
            additionalBaggageRepository.save(object);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Se ha creado con exito el equipamiento extra.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("createAdditionalBaggage: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Ha ocurrido un error a la hora de crear el equipamiento extra", 404, LocalDateTime.now()));
        }
    }

    public ResponsePaginatedVO<AdditionalBaggageReducedVO> getAllAdditionalBaggagesReduced(PageVO pageVO) {
        try {
            Page<AdditionalBaggageReducedVO> additionalBaggages = additionalBaggageRepository.getAllAdditionalBaggageReduced(pageVO.toPageable());
            ResponsePaginatedVO<AdditionalBaggageReducedVO> data = new ResponsePaginatedVO<>();
            data.setObjects(additionalBaggages.getContent());
            data.setHasNextPage(additionalBaggages.hasNext());
            data.setHasPreviousPage(additionalBaggages.hasPrevious());
            data.setCurrentPage(additionalBaggages.getNumber());
            data.setTotalPages(additionalBaggages.getTotalPages());
            data.setMessages(new MessageResponseVO("Se ha podido obtener todos los equipamientos.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllAdditionalBaggagesReduced: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Ha ocurrido un error a la hora de obtener todos los equipamientos", 404, LocalDateTime.now()));
        }
    }
}
