package com.example.skystayback.dtos.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomFormVO {
    private Integer total_rooms;
    private Integer capacity;
    private String type;
}
