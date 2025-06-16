package com.example.skystayback.dtos.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private Long id;
    private String flightCode;
    private String orderCode;

    // Información del vuelo
    private String departureDateTime;
    private String arrivalDateTime;
    private String departureTime;
    private String arrivalTime;
    private String flightDate;
    private String status;
    private String statusText;
    private String duration;

    // Aerolínea
    private String airlineName;
    private String airlineCode;
    private String airlineIataCode;
    private String airlineImage;
    private String airlinePhone;
    private String airlineEmail;
    private String airlineWebsite;

    // Aeropuertos
    private String departureAirportName;
    private String departureAirportCode;
    private String departureAirportIataCode;
    private String departureCityName;
    private String departureCountryName;
    private String departureTerminal;
    private String departureGate;

    private String arrivalAirportName;
    private String arrivalAirportCode;
    private String arrivalAirportIataCode;
    private String arrivalCityName;
    private String arrivalCountryName;
    private String arrivalTerminal;
    private String arrivalGate;

    // Información del asiento
    private String seatNumber;
    private String seatRow;
    private String seatColumn;
    private String seatClass;
    private Double seatPrice;

    // Información del pasajero
    private String passengerName;
    private String passengerSurnames;
    private String passengerNif;
    private String passengerPhone;
    private String passengerEmail;

    // Información del avión
    private String airplaneModel;
    private String airplaneCode;
    private String airplaneRegistration;
    private String airplaneType;
    private Integer airplaneCapacity;
    private String airplaneManufacturer;

    // Facturación
    private Double totalAmount;
    private Double discount;
    private String orderStatus;
    private boolean hasBill;

    // Estado del vuelo
    private boolean isPast;
    private boolean isCurrent;
    private boolean isFuture;
    private boolean canCancel;
    private boolean canReview;
    private boolean hasReview;
    private Long reviewId;

    // Servicios adicionales
    private List<String> meals;
    private String additionalBaggage;
    private Double baggageWeight;
    private Double baggageAmount;

    // Imágenes
    private String image;
    private List<String> images;
}
