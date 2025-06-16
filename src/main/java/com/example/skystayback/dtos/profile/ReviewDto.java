package com.example.skystayback.dtos.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String type; // "hotel", "apartment", "airline"
    private Float rating;
    private String title;
    private String comment;
    private String pros;
    private String cons;
    private Integer helpfulCount;
    private String createdAt;
    private String updatedAt;

    // Información del lugar/servicio reseñado
    private String entityName;
    private String entityCode;
    private String entityImage;
    private String cityName;
    private String countryName;
    private Integer stars; // Solo para hoteles/apartamentos

    // Para hoteles y apartamentos
    private String address;
    private String phone;
    private String email;
    private String website;
    private String amenities;

    // Para aerolíneas
    private String iataCode;

    // Estado de la reseña
    private boolean canEdit;
    private boolean canDelete;
    private boolean isVerifiedBooking;

    // Información del usuario (opcional)
    private String userName;
    private String userImage;
    private boolean isAnonymous;
}
