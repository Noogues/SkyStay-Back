package com.example.skystayback.enums;

public enum Status {
    ACTIVE,        // En operación normal y disponible para vuelos.
    INACTIVE,      // Temporalmente fuera de servicio, pero no dañado.
    IN_REPAIR,     // En proceso de mantenimiento o reparación.
    DECOMMISSIONED, // Retirado del servicio y no volverá a operar.
    TESTING        // En fase de pruebas antes de entrar en operación.
}
