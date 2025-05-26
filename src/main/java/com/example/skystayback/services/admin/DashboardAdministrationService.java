package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.flights.FlightsDetailsVO;
import com.example.skystayback.dtos.user.UserAdminVO;
import com.example.skystayback.models.Flight;
import com.example.skystayback.models.OrderApartment;
import com.example.skystayback.models.OrderFlight;
import com.example.skystayback.models.OrderHotel;
import com.example.skystayback.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DashboardAdministrationService {

    private final FlightRepository flightRepository;
    private final OrderHotelRepository orderHotelRepository;
    private final OrderFlightRepository orderFlightRepository;
    private final OrderApartmentRepository orderApartmentRepository;
    private final UserRepository userRepository;

    /**
     * Obtiene la cantidad total de ganacias generadas por las reservas de hoteles, vuelos y apartamentos por parte de la plataforma.
     *
     * @return ResponseVO con el total de reservas de hoteles.
     */
    public ResponseVO<Double> getTotalRevenue() {
        try{
            double totalRevenue = 0.0;

            totalRevenue += orderHotelRepository.findAll().stream()
                    .mapToDouble(order -> order.getAmount() * order.getDiscount())
                    .sum();
            totalRevenue += orderFlightRepository.findAll().stream()
                    .mapToDouble(order -> order.getAmount() * order.getDiscount())
                    .sum();

            totalRevenue += orderApartmentRepository.findAll().stream()
                    .mapToDouble(order -> order.getAmount() * order.getDiscount())
                    .sum();

            return new ResponseVO<>(new DataVO<>(totalRevenue), new MessageResponseVO("Se ha obtenido las ganacias generales entre hoteles, apartamentos y vuelos", 200, LocalDateTime.now()));
        }catch (Exception e){
            System.out.println("getTotalRevenue: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(0.0), new MessageResponseVO("Ha ocurrido un error al obtener las ganancias generales", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene los ultimos 5 usuarios registrados en la plataforma.
     *
     * @return ResponseVO con el total de reservas.
     */
    public ResponseVO<List<UserAdminVO>> getLast5User(){
        try {
            List<UserAdminVO> users = userRepository.getLast5User();
            return new ResponseVO<>(new DataVO<>(users), new MessageResponseVO("Se han obtenido los ultimos 5 usuarios", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getLast5User: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Ha ocurrido un error al obtener los ultimos 5 usuarios", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene los ultimos 5 vuelos registrados en la plataforma.
     *
     * @return ResponseVO con el total de reservas.
     */
    public ResponseVO<List<FlightsDetailsVO>> getLast5Flights() {
        try {
            List<FlightsDetailsVO> flightsDetailsVO = flightRepository.getLast5Flights();
            return new ResponseVO<>(new DataVO<>(flightsDetailsVO), new MessageResponseVO("Se han obtenido los ultimos 5 vuelos", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getLast5Flights: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(null), new MessageResponseVO("Ha ocurrido un error al obtener los ultimos 5 vuelos", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene el total de vuelos activos en la plataforma.
     *
     * @return ResponseVO con el total de vuelos activos.
     */
    public ResponseVO<Integer> getTotalFlightsActive() {
        try {
            List<Flight> totalFlights = flightRepository.totalFlightsScheduled();
            return new ResponseVO<>(new DataVO<>(totalFlights.size()), new MessageResponseVO("Se ha obtenido el total de vuelos", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getTotalFlights: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(0), new MessageResponseVO("Ha ocurrido un error al obtener el total de vuelos", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene el numero total de usuarios registrados en la plataforma.
     *
     * @return ResponseVO con el total de reservas de hoteles.
     */
    public ResponseVO<Integer> totalUsersAcounts(){
        try{
            Integer totalUsers = userRepository.findAll().size();
            return new ResponseVO<>(new DataVO<>(totalUsers), new MessageResponseVO("Se ha obtenido el total de usuarios", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("totalUsersAcounts: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(0), new MessageResponseVO("Ha ocurrido un error al obtener el total de usuarios", 404, LocalDateTime.now()));
        }
    }
}
