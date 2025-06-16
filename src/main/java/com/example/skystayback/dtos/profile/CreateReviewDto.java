package com.example.skystayback.dtos.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewDto {

    @NotBlank(message = "El tipo de reseña es obligatorio")
    @Pattern(regexp = "^(hotel|apartment|airline)$", message = "Tipo de reseña no válido")
    private String type;

    @NotBlank(message = "El código de la entidad es obligatorio")
    private String entityCode;

    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "1.0", message = "La calificación mínima es 1.0")
    @DecimalMax(value = "5.0", message = "La calificación máxima es 5.0")
    private Float rating;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
    private String title;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(min = 20, max = 1000, message = "El comentario debe tener entre 20 y 1000 caracteres")
    private String comment;

    @Size(max = 500, message = "Los aspectos positivos no pueden exceder 500 caracteres")
    private String pros;

    @Size(max = 500, message = "Los aspectos negativos no pueden exceder 500 caracteres")
    private String cons;

    // ID de la reserva (opcional, para verificar que el usuario realmente se hospedó/voló)
    private Long bookingId;

    // Para verificar que es una reseña legítima
    private boolean isVerifiedBooking;
}
