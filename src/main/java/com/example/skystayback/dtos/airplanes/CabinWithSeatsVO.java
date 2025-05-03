package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabinWithSeatsVO {
    private Long cabinId;
    private String seatconfigurationName;
    private Integer rowStart;
    private Integer rowEnd;
    private SeatClass seatClass;
    private List<SeatVO> seats;
}