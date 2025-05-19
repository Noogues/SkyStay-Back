package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.flights.FlightsTableVO;
import com.example.skystayback.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FlightsAdministrationService {

    private final FlightRepository flightRepository;

    public ResponsePaginatedVO<FlightsTableVO> getAllFlights(PageVO pageVO) {
        try {
            Page<FlightsTableVO> flights = flightRepository.findAllFlights(pageVO.toPageable());
            ResponsePaginatedVO<FlightsTableVO> data = new ResponsePaginatedVO<>();
            data.setObjects(flights.getContent());
            data.setHasNextPage(flights.hasNext());
            data.setHasPreviousPage(flights.hasPrevious());
            data.setCurrentPage(flights.getNumber());
            data.setTotalPages(flights.getTotalPages());
            data.setMessages(new MessageResponseVO("Hoteles recuperados con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los hoteles", 404, LocalDateTime.now()));
        }
    }

}

