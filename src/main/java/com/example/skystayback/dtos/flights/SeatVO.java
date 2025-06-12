package com.example.skystayback.dtos.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatVO {
    private Long id;
    private String seatRow;
    private String seatColumn;
    private Boolean state;
}
