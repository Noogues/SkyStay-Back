package com.example.skystayback.dtos.flights;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightPurchaseVO {
    private String name;
    private String surnames;
    private String email;
    private String nif;
    private String phone;
    private String seatColumn;
    private String seatRow;
    private SeatClass seatClass;
}
