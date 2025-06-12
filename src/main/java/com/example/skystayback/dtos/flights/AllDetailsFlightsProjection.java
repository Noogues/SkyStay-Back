package com.example.skystayback.dtos.flights;
import java.math.BigDecimal;

public interface AllDetailsFlightsProjection {
    String getDepartureTime();
    Integer getStatus();
    String getDateTime();
    String getDateTimeArrival();

    String getAirlineCode();
    String getAirlineName();
    String getAirlineImage();
    String getAirlinePhone();
    String getAirlineEmail();
    String getAirlineWebsite();
    String getAirlineIataCode();

    String getDepartureAirportCode();
    String getDepartureAirportName();
    String getDepartureAirportIataCode();
    String getDepartureAirportDescription();
    String getDepartureTerminal();
    String getDepartureGate();
    BigDecimal getDepartureLatitude();
    BigDecimal getDepartureLongitude();
    String getDepartureTimezone();
    String getDepartureCityName();
    String getDepartureCountryName();

    String getArrivalAirportCode();
    String getArrivalAirportName();
    String getArrivalAirportIataCode();
    String getArrivalAirportDescription();
    String getArrivalTerminal();
    String getArrivalGate();
    BigDecimal getArrivalLatitude();
    BigDecimal getArrivalLongitude();
    String getArrivalTimezone();
    String getArrivalCityName();
    String getArrivalCountryName();

    String getAirplaneCode();
    String getAirplaneModel();
    String getAirplaneRegistrationNumber();
    Integer getAirplaneYear();
    Integer getAirplaneCapacity();
    String getAirplaneManufacturer();
    String getAirplaneTypeName();
}

