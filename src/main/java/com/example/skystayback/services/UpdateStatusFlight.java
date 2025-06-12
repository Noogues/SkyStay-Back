package com.example.skystayback.services;

import com.example.skystayback.enums.FlightStatus;
import com.example.skystayback.models.Flight;
import com.example.skystayback.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UpdateStatusFlight {

    @Autowired
    private FlightRepository flightRepository;

    @Scheduled(fixedRate = 60000)
    public void updateFlightStatuses() {

        LocalDateTime now = LocalDateTime.now();
        List<Flight> allFlights = flightRepository.findAllNotLandedCancelled();

        for (Flight flight : allFlights) {
            LocalDateTime departure = flight.getDateTime();
            LocalDateTime arrival = flight.getDateTimeArrival();

            long minutesToDeparture = Duration.between(now, departure).toMinutes();
            long minutesSinceDeparture = Duration.between(departure, now).toMinutes();
            long minutesSinceArrival = Duration.between(arrival, now).toMinutes();

            if (minutesToDeparture > 35) {
                flight.setStatus(FlightStatus.SCHEDULED);
            } else if (minutesToDeparture > 30) {
                flight.setStatus(FlightStatus.BOARDING);
            } else if (minutesSinceDeparture >= 0 && minutesSinceArrival < 0) {
                flight.setStatus(FlightStatus.IN_FLIGHT);
            } else if (minutesSinceArrival >= 0) {
                flight.setStatus(FlightStatus.LANDED);
            }

            flightRepository.save(flight);
        }
    }
}
