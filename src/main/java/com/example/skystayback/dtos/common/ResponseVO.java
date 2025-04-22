package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseVO<T> {

    private DataVO<T> response;
    private MessageResponseVO messages;

}

// Caso de uso 1: Usar la clase ResponseVO con ambos campos, response y messages.
// Este caso es útil cuando necesitas devolver datos (response) junto con mensajes informativos o de error (messages).
// Por ejemplo, ResponseVO<HotelRatingVO> o ResponseVO<ApartmentRatingVO>.

// Caso de uso 2: Usar la clase ResponseVO solo con el campo messages.
// Este caso es útil cuando no necesitas devolver datos, pero sí mensajes informativos o de error.
// Por ejemplo, ResponseVO<Void> o ResponseVO<String>.
