package com.example.skystayback.enums;

import lombok.Getter;

@Getter
public enum FlightStatus {
    SCHEDULED(0),   // Programado | 0
    BOARDING(1),    // Embarcando | 1
    IN_FLIGHT(2),   // En vuelo | 2
    LANDED(3),      // Aterrizado | 3
    CANCELLED(4),   // Cancelado | 4
    DELAYED(5),     // Retrasado | 5
    DIVERTED(6);    // Desviado a otro aeropuerto | 6

    private final int code;

    FlightStatus(int code) {
        this.code = code;
    }

    public static FlightStatus fromCode(int code) {
        for (FlightStatus status : FlightStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de estado no válido: " + code);
    }
}