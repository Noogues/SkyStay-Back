package com.example.skystayback.enums;

public enum BookingStatus {
    CONFIRMED,        // Reserva confirmada: la reserva ha sido aprobada y asegurada.
    CHECKED_IN,       // Reserva con check-in: el cliente se ha registrado para el vuelo.
    CANCELLED,        // Reserva cancelada: la reserva ha sido anulada por el cliente o por la aerolínea.
    PENDING,          // Reserva pendiente: la reserva está en espera de ser confirmada.
    NO_SHOW,          // No se presentó: el cliente no se presentó para el vuelo.
}
