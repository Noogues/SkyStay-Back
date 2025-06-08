package com.example.skystayback.dtos.flights;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabinsVO {
    private Long id;
    private SeatClass seatClass;
}
