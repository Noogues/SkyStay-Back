package com.example.skystayback.dtos.flights;

import com.example.skystayback.converter.TimeParser;
import com.example.skystayback.dtos.airline.AirlineTableVO;
import com.example.skystayback.dtos.airports.AirportAdminVO;
import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.dtos.city.CountryVO;
import com.example.skystayback.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDetailsFlightsVO {
    private LocalTime depatureTime;
    private FlightStatus flightStatus;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeArrival;
    private AirlineTableVO airline;
    private AirportAdminVO departureAirport;
    private AirportAdminVO arrivalAirport;
    private AirplaneVO airplane;

    public AllDetailsFlightsVO(AllDetailsFlightsProjection p) {
        this.depatureTime = p.getDepartureTime() != null ? LocalTime.parse(p.getDepartureTime()) : null;
        this.flightStatus = FlightStatus.fromCode(p.getStatus());
        this.dateTime = p.getDateTime() != null ? TimeParser.parseDateTime(p.getDateTime()) : null;
        this.dateTimeArrival = p.getDateTimeArrival() != null ? TimeParser.parseDateTime(p.getDateTimeArrival()) : null;

        this.airline = new AirlineTableVO(
                p.getAirlineCode(),
                p.getAirlineName(),
                p.getAirlineImage(),
                p.getAirlinePhone(),
                p.getAirlineEmail(),
                p.getAirlineWebsite(),
                p.getAirlineIataCode()
        );

        this.departureAirport = new AirportAdminVO(
                p.getDepartureAirportCode(),
                p.getDepartureAirportName(),
                p.getDepartureAirportIataCode(),
                p.getDepartureAirportDescription(),
                p.getDepartureTerminal(),
                p.getDepartureGate(),
                p.getDepartureLatitude(),
                p.getDepartureLongitude(),
                p.getDepartureTimezone(),
                new CityVO(
                        p.getDepartureCityName(),
                        new CountryVO(p.getDepartureCountryName())
                )
        );

        this.arrivalAirport = new AirportAdminVO(
                p.getArrivalAirportCode(),
                p.getArrivalAirportName(),
                p.getArrivalAirportIataCode(),
                p.getArrivalAirportDescription(),
                p.getArrivalTerminal(),
                p.getArrivalGate(),
                p.getArrivalLatitude(),
                p.getArrivalLongitude(),
                p.getArrivalTimezone(),
                new CityVO(
                        p.getArrivalCityName(),
                        new CountryVO(p.getArrivalCountryName())
                )
        );

        this.airplane = new AirplaneVO(
                p.getAirplaneCode(),
                p.getAirplaneModel(),
                p.getAirplaneRegistrationNumber(),
                p.getAirplaneYear(),
                p.getAirplaneCapacity(),
                p.getAirplaneManufacturer(),
                p.getAirplaneTypeName()
        );
    }
}
