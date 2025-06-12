package com.example.skystayback.dtos.common;

import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Data
public class RealizarReservaRequest {
    private Double total;
    private List<RoomSelectionRequest> rooms;
    private String accommodationCode;
    private String accommodationType;
    private LocalDate startDate;
    private LocalDate endDate;
}

