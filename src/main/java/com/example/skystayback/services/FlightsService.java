package com.example.skystayback.services;

import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.PageVO;
import com.example.skystayback.dtos.common.ResponsePaginatedVO;
import com.example.skystayback.dtos.flights.FlightClientVO;
import com.example.skystayback.repositories.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class FlightsService {

    private final FlightRepository flightRepository;

    public ResponsePaginatedVO<FlightClientVO> getAllFlights(PageVO page, String origin, String destination, String airline, Float price) {
        try {
            Page<FlightClientVO> airplanePage = flightRepository.findAllClientFlightsWithFilters(
                    page.toPageable(), origin, destination, airline, price);
            ResponsePaginatedVO<FlightClientVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airplanePage.getContent());
            data.setHasNextPage(airplanePage.hasNext());
            data.setHasPreviousPage(airplanePage.hasPrevious());
            data.setCurrentPage(airplanePage.getNumber());
            data.setTotalPages(airplanePage.getTotalPages());
            data.setMessages(new MessageResponseVO("Vuelos recuperados con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllFlights: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los vuelos", 404, LocalDateTime.now()));
        }
    }
}
