package com.example.skystayback.services;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.flights.*;
import com.example.skystayback.enums.StatusOrder;
import com.example.skystayback.models.FlightSeatStatus;
import com.example.skystayback.models.OrderFlight;
import com.example.skystayback.models.SeatBooking;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.FlightRepository;
import com.example.skystayback.repositories.FlightSeatStatusRepository;
import com.example.skystayback.repositories.OrderFlightRepository;
import com.example.skystayback.repositories.SeatBookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.skystayback.dtos.meal.MealVO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class FlightsService {

    private final FlightRepository flightRepository;
    private final UserService userService;
    private final OrderFlightRepository orderFlightRepository;
    private final FlightSeatStatusRepository flightSeatStatusRepository;
    private final SeatBookingRepository seatBookingRepository;

    /**
     * Recupera todos los vuelos con paginación y filtros opcionales.
     *
     * @param page Paginación de la consulta.
     * @param origin Filtro por origen del vuelo.
     * @param destination Filtro por destino del vuelo.
     * @param airline Filtro por aerolínea del vuelo.
     * @param price Filtro por precio del vuelo.
     * @return ResponsePaginatedVO con los vuelos encontrados y metadatos de paginación.
     */
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

    /**
     * Mediante el código de vuelo, recupera toda la informacion relevante del vuelo.
     */
    public ResponseVO<AllDetailsFlightsVO> getFlightDetails(String flightCode) {
        try {
            AllDetailsFlightsProjection flight = flightRepository.findFlightDetailsById(flightCode).orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
            AllDetailsFlightsVO flightParsed = new AllDetailsFlightsVO(flight);
            return new ResponseVO<>(new DataVO<>(flightParsed), new MessageResponseVO("Vuelo recuperado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getFlightDetails: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar el vuelo", 404, LocalDateTime.now()));
        }
    }

    /**
     * Mediante en código de vuelo, recupera las comidas disponibles para el vuelo.
     */
    public ResponseVO<List<MealVO>> getMealsByFlightCode(String flightCode) {
        try {
            List<MealVO> meals = flightRepository.findMealsByFlightCode(flightCode);
            return new ResponseVO<>(new DataVO<>(meals), new MessageResponseVO("Comidas recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getMealsByFlightCode: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las comidas", 404, LocalDateTime.now()));
        }
    }

    /**
     * Mediante el código de vuelo, recupera las cabinas disponibles para el vuelo.
     */
    public ResponseVO<List<CabinFlightDetailsVO>> getCabinsForFlightByCode(String flightCode) {
        try {
            List<CabinFlightDetailsVO> cabins = flightRepository.getCabinsForFlightByCode(flightCode);
            for (CabinFlightDetailsVO cabin : cabins) {
                cabin.setSeats(flightRepository.getSeatsForCabin(cabin.getId()));
            }
            return new ResponseVO<>(new DataVO<>(cabins), new MessageResponseVO("Cabinas recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getCabinsForFlightByCode: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las cabinas", 404, LocalDateTime.now()));
        }
    }

    /**
     * Permite comprar tickets de vuelo, reservando los asientos seleccionados.
     *
     * @param flightCode Código del vuelo.
     * @param flightPurchaseDTO Lista de asientos a reservar y datos del pasajero.
     * @param token Token de autenticación del usuario.
     * @return ResponseVO con el resultado de la operación.
     */
    @Transactional
    public ResponseVO<Void> purchaseTicketsFlight(String flightCode, List<FlightPurchaseVO> flightPurchaseDTO, String token) {
        try {
            Double totalAmount = 0.0;
            User user = userService.getUser(token);
            OrderFlight of = new OrderFlight();
            String code = userService.generateShortUuid();
            if (orderFlightRepository.existsByCode(code)) {
                code = userService.generateShortUuid();
            }
            of.setCode(code);
            of.setAmount(0.0);
            of.setDiscount(0.0);
            of.setStatus(StatusOrder.Paid);
            of.setBill(null);
            orderFlightRepository.save(of);

            for (FlightPurchaseVO purchase : flightPurchaseDTO) {
                FlightSeatStatus flightSeatStatus = flightSeatStatusRepository.findSeatByFlightCodeAndRowAndColumn(flightCode, purchase.getSeatRow(), purchase.getSeatColumn(), purchase.getSeatClass())
                        .orElseThrow(() -> new RuntimeException("Asiento no encontrado"));
                if (seatBookingRepository.existsByFlightSeatStatusAndEmailAndNifAndPhone(
                        flightSeatStatus, purchase.getEmail(), purchase.getNif(), purchase.getPhone())) {
                    return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Ya existe una reserva con estos datos en el mismo vuelo.", 400, LocalDateTime.now()));
                }
                flightSeatStatus.setState(false);
                flightSeatStatusRepository.save(flightSeatStatus);
                SeatBooking seatBooking = new SeatBooking();
                seatBooking.setName(purchase.getName());
                seatBooking.setSurnames(purchase.getSurnames());
                seatBooking.setEmail(purchase.getEmail());
                seatBooking.setNif(purchase.getNif());
                seatBooking.setPhone(purchase.getPhone());
                seatBooking.setOrderFlight(of);
                seatBooking.setFlightSeatStatus(flightSeatStatus);
                seatBooking.setUser(user);
                seatBookingRepository.save(seatBooking);
                totalAmount += flightSeatStatus.getPrice();
            }
            of.setAmount(totalAmount);
            orderFlightRepository.save(of);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Asientos reservados y tickets de vuelo generados", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("purchaseFlight: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al comprar el vuelo", 404, LocalDateTime.now()));
        }
    }
}
