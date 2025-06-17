package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.flights.*;
import com.example.skystayback.enums.FlightStatus;
import com.example.skystayback.models.*;
import com.example.skystayback.repositories.*;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class FlightsAdministrationService {

    private final FlightRepository flightRepository;
    private final UserService userService;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final AirplaneRepository airplaneRepository;
    private final AirplaneCabinRepository airplaneCabinRepository;
    private final FlightSeatStatusRepository flightSeatStatusRepository;
    private final SeatRepository seatRepository;
    private final MealRepository mealRepository;
    private final FlightMealRepository flightMealRepository;

    /**
     * Recupera todos los vuelos paginados.
     * @param pageVO Objeto que contiene la información de paginación.
     * @return ResponsePaginatedVO con la lista de vuelos y metadatos de paginación.
     */
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
            System.out.println("getAllFlights: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los hoteles", 404, LocalDateTime.now()));
        }
    }

    /**
     * Recupera los detalles de un vuelo específico basado en su código.
     * @param code Código del vuelo a recuperar.
     * @return ResponseVO con los detalles del vuelo o un mensaje de error.
     */
    public ResponseVO<FlightsDetailsVO> getDetailsByCode(String code) {
        try {
            FlightsDetailsVO flightDetails = flightRepository.findFlightDetailsByCode(code);
            return new ResponseVO<>(new DataVO<>(flightDetails), new MessageResponseVO("Vuelo recuperado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getDetailsByCode: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar el vuelo", 500, LocalDateTime.now()));
        }
    }

    /**
     * Crea un nuevo vuelo basado en la información proporcionada.
     * @param form Objeto FlightCreateVO que contiene los detalles del vuelo a crear.
     * @return ResponseVO con el resultado de la operación.
     */
    public ResponseVO<Void> createFlight(FlightCreateVO form){
        try{
            Flight flight = new Flight();
            flight.setCode(userService.generateShortUuid());
            flight.setStatus(FlightStatus.SCHEDULED);
            flight.setDepartureTime(form.getDateTime().toLocalTime());
            flight.setDateTime(form.getDateTime());
            flight.setDateTimeArrival(form.getDateTimeArrival());
            Airline airline = airlineRepository.findById(form.getAirlineId()).orElseThrow(() -> new IllegalArgumentException("Aerolinea no encontrada"));
            flight.setAirline(airline);

            Airport depatureAirport = airportRepository.findById(form.getDepartureAirportId()).orElseThrow(() -> new IllegalArgumentException("Aeropuerto de saliuda no encontrado"));
            flight.setDepartureAirport(depatureAirport);

            Airport arrivalAirport = airportRepository.findById(form.getArrivalAirportId()).orElseThrow(() -> new IllegalArgumentException("Aeropuerto de llegada no encontrado"));
            flight.setArrivalAirport(arrivalAirport);

            Airplane airplane = airplaneRepository.findById(form.getAirplaneId()).orElseThrow(() -> new IllegalArgumentException("Avion no encontrado"));
            flight.setAirplane(airplane);

            flight = flightRepository.save(flight);

            for (CabinsPriceVO cabin : form.getCabins()) {
                if (cabin.getPrice() <= 0) {
                    return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("El precio de la cabina debe ser mayor a 0", 400, LocalDateTime.now()));
                }
                List<Seat> seats = seatRepository.findSeatsByCabinId(cabin.getId());
                for (Seat seat : seats) {
                    FlightSeatStatus item = new FlightSeatStatus();
                    item.setFlight(flight);
                    item.setSeat(seat);
                    item.setState(true);
                    item.setPrice(cabin.getPrice());
                    flightSeatStatusRepository.save(item);
                }
            }

            for (MealFlightsVO meal : form.getMeals()) {
                if (meal.getCode() == null || meal.getCode().isEmpty()) {
                    return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("El código de la comida no puede estar vacío", 400, LocalDateTime.now()));
                }
                Meal mealItem = mealRepository.findByCode(meal.getCode()).orElseThrow(() -> new IllegalArgumentException("Comida no encontrada con el código: " + meal.getCode()));
                FlightMeal flightMeal = new FlightMeal();
                flightMeal.setFlight(flight);
                flightMeal.setMeal(mealItem);
                flightMealRepository.save(flightMeal);
            }

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Vuelo creado con éxito", 200, LocalDateTime.now()));
        }catch (Exception e) {
            System.out.println("createFlight: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear el vuelo", 500, LocalDateTime.now()));
        }
    }

    /**
     * Genera vuelos aleatorios basados en las aerolíneas, aeropuertos y aviones disponibles.
     * @param count Número de vuelos a generar.
     * @return ResponseVO con el resultado de la operación.
     */
    public ResponseVO<Void> generateRandomFlights(int count) {
        try {
            List<Airline> airlines = airlineRepository.findAll();
            List<Airport> airports = airportRepository.findAll();
            List<Airplane> airplanes = airplaneRepository.findAll();

            if (airlines.isEmpty() || airports.size() < 2 || airplanes.isEmpty()) {
                return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Datos insuficientes para generar vuelos", 400, LocalDateTime.now()));
            }

            Random random = new Random(System.nanoTime());
            int createdCount = 0;

            for (int i = 0; i < count; i++) {
                // Seleccionar aleatoriamente entidades
                Airline airline = airlines.get(random.nextInt(airlines.size()));
                Airplane airplane = airplanes.get(random.nextInt(airplanes.size()));

                // Barajar aeropuertos para obtener combinaciones más variadas
                Collections.shuffle(airports, random);

                Airport departureAirport = airports.get(0); // Corregido: usar get(0)
                Airport arrivalAirport = null;

                for (int j = 1; j < airports.size(); j++) {
                    if (!airports.get(j).getId().equals(departureAirport.getId())) {
                        arrivalAirport = airports.get(j);
                        break;
                    }
                }

                // Si no hay llegada distinta, omitir este vuelo
                if (arrivalAirport == null) {
                    continue;
                }

                // Generar fecha aleatoria dentro de las próximas 4 semanas
                LocalDate startDate = LocalDate.of(2025, 6, 17);
                LocalDate endDate = LocalDate.of(2025, 7, 1);
                LocalDate randomDate = startDate.plusDays(random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1));

                // Generar hora aleatoria entre 00:00:00 y 23:59:59
                LocalTime randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));
                LocalDateTime departureTime = LocalDateTime.of(randomDate, randomTime);

                // Duración de vuelo aleatoria entre 1 y 6 horas
                int durationHours = 1 + random.nextInt(6);
                LocalDateTime arrivalTime = departureTime.plusHours(durationHours);

                // Crear objeto FlightCreateVO simulado
                FlightCreateVO form = new FlightCreateVO();
                form.setAirlineId(airline.getId());
                form.setAirplaneId(airplane.getId());
                form.setDepartureAirportId(departureAirport.getId());
                form.setArrivalAirportId(arrivalAirport.getId());
                form.setDateTime(departureTime);
                form.setDateTimeArrival(arrivalTime);

                // Inicializar listas de cabinas y comidas
                form.setCabins(new ArrayList<>());
                form.setMeals(new ArrayList<>());

                // Generar cabinas con precios aleatorios
                List<AirplaneCabin> airplaneCabins = airplaneCabinRepository.findByAirplaneId(airplane.getId());
                for (AirplaneCabin airplaneCabin : airplaneCabins) {
                    CabinsPriceVO cabin = new CabinsPriceVO();
                    cabin.setId(airplaneCabin.getId());
                    cabin.setPrice((float) (45 + random.nextInt(606)));
                    form.getCabins().add(cabin);
                }

                // Seleccionar aleatoriamente entre 3 y 6 comidas
                int randomMealCount = 3 + random.nextInt(4); // 4 = 6 - 3 + 1
                List<Meal> allMeals = mealRepository.findAll();
                Collections.shuffle(allMeals, random);
                List<Meal> selectedMeals = allMeals.subList(0, Math.min(randomMealCount, allMeals.size()));

                for (Meal meal : selectedMeals) {
                    MealFlightsVO mealVO = new MealFlightsVO();
                    mealVO.setCode(meal.getCode());
                    form.getMeals().add(mealVO);
                }

                // Verificar si ya existe un vuelo similar esa semana
                if (isSimilarFlightExists(form)) {
                    continue;
                }

                // Crear el vuelo
                createFlight(form);
                createdCount++;
            }

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO(createdCount + " vuelos generados exitosamente", 200, LocalDateTime.now()));

        } catch (Exception e) {
            System.out.println("generateRandomFlights: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error generando vuelos aleatorios", 500, LocalDateTime.now()));
        }
    }

    /**
     * Verifica si ya existe un vuelo similar en la misma semana.
     * @param form Objeto FlightCreateVO que contiene los detalles del vuelo a verificar.
     * @return true si existe un vuelo similar, false de lo contrario.
     */
    private boolean isSimilarFlightExists(FlightCreateVO form) {
        LocalDate startOfWeek = form.getDateTime().with(DayOfWeek.MONDAY).toLocalDate();
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Ajustar los parámetros para que coincidan con los tipos esperados
        List<Flight> similarFlights = flightRepository.findSimilarFlights(
                form.getAirlineId(),
                form.getDepartureAirportId(),
                form.getArrivalAirportId(),
                form.getAirplaneId(),
                startOfWeek.atStartOfDay(),
                endOfWeek.atTime(LocalTime.MAX)
        );

        for (Flight flight : similarFlights) {
            long hoursDiff = Duration.between(flight.getDateTime(), form.getDateTime()).abs().toHours();
            if (hoursDiff < 2) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recupera la información de las cabinas de un avión específico.
     * @param airplaneId ID del avión del cual se desea obtener la información de las cabinas.
     * @return ResponseVO con la lista de cabinas y un mensaje de éxito o error.
     */
    public ResponseVO<List<CabinsVO>> getCabinsInfo(Long airplaneId) {
        try {
            List<CabinsVO> cabinsVO = airplaneCabinRepository.getCabinsAirplaneId(airplaneId);
            return new ResponseVO<>(new DataVO<>(cabinsVO), new MessageResponseVO("Información de cabinas recuperada con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getCabinsInfo: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar la información de cabinas", 500, LocalDateTime.now()));
        }
    }

    /**
     * Recupera las cabinas disponibles para un vuelo específico basado en su código.
     * @param code Código del vuelo del cual se desean obtener las cabinas.
     * @return ResponseVO con la lista de cabinas y un mensaje de éxito o error.
     */
    public ResponseVO<List<CabinInfoVO>> getCabinsByCode(String code) {
        try {
            List<CabinInfoVO> cabins = flightRepository.findCabinsByFlightCode(code);
            return new ResponseVO<>(new DataVO<>(cabins), new MessageResponseVO("Cabinas recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getCabinsByCode: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las cabinas", 500, LocalDateTime.now()));
        }
    }
}