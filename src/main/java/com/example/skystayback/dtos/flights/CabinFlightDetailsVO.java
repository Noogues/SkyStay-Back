package com.example.skystayback.dtos.flights;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CabinFlightDetailsVO {
    private Long id;
    private SeatClass seatClass;
    private String seatPattern;
    private Long totalSeats;
    private Long availableSeats;
    private Float price;
    private List<SeatVO> seats = new ArrayList<>();

    public CabinFlightDetailsVO(Long id, SeatClass seatClass, String pattern, Long totalSeats, Long availableSeats, Float price) {
        this.id = id;
        this.seatClass = seatClass;
        this.seatPattern = pattern;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
    }
}
