package com.example.skystayback.dtos.common;

import com.example.skystayback.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsVO {
    private Long roomConfigId;
    private Integer capacity;
    private RoomType roomType;
    private Long availableCount;
    private Double price;

    public RoomDetailsVO(Long id, Integer capacity, RoomType type, long availableRooms, Double price) {
        this.roomConfigId = id;
        this.capacity = capacity;
        this.roomType = type;
        this.availableCount = availableRooms;
        this.price = price;
    }
}