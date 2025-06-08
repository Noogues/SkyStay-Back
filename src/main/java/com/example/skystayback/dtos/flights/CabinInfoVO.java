package com.example.skystayback.dtos.flights;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CabinInfoVO {
    private Long id;
    private SeatClass seatClass;
    private Long totalSeats;
    private Long availableSeats;
    private Float price;
}
