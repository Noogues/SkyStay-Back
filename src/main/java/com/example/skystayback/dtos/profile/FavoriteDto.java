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
public class FavoriteDto {
    private Long id;
    private String type; // "hotel" or "apartment"
    private String code;
    private String name;
    private Integer stars;
    private String description;
    private String address;
    private String postalCode;
    private String phone;
    private String email;
    private String website;
    private String amenities;

    // Ubicación
    private String cityName;
    private String countryName;
    private String continent;

    // Precios (precio mínimo disponible)
    private Double minPrice;
    private Double maxPrice;
    private String currency;

    // Imágenes
    private String mainImage;
    private List<String> images;

    // Información adicional
    private String addedDate;
    private boolean isAvailable;
    private Integer totalRooms;
    private Double averageRating;
    private Integer totalReviews;

    // Para mostrar en la UI
    private String priceDisplay;
    private String locationDisplay;
    private String featuresDisplay;

    // Enlaces
    private String viewUrl;
    private String bookUrl;
}