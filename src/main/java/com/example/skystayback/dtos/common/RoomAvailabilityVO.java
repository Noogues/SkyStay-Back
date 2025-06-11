package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailabilityVO {
    private String roomId;
    private List<DateRangeVO> availableDateRanges;
}