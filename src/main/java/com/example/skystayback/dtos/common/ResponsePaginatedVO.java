package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePaginatedVO<T> {

    private List<T> objects = new ArrayList<>();

    // Todos estos atributos son necesarios para la paginación en el front-end
    // Si la paginacion no es requerida, se puede omitir estos atributos.
    private boolean hasNextPage = false;
    private boolean hasPreviousPage = false;
    private int currentPage = 0;
    private int totalPages = 0;

    private MessageResponseVO messages;

    public ResponsePaginatedVO(MessageResponseVO messages) {
        this.messages = messages;
    }
}


