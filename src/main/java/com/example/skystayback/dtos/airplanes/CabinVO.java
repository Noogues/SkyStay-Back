package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.SeatClass;
import com.example.skystayback.models.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabinVO {

    private SeatClass seatClass;
    private Integer rowStart;
    private Integer rowEnd;

    private SeatConfigurationAllVO seatConfiguration;

    List<SeatVO> seats;
}
