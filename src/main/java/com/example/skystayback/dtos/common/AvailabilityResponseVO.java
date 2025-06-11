package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityResponseVO {
    private List<RoomAvailabilityVO> objects;

}
