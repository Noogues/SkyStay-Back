package com.example.skystayback.dtos.hotel;

import com.example.skystayback.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowRoomsDetails {

    private Integer roomCapacity;
    private RoomType roomType;
    private String image;

    private List<RoomVO> rooms;
}
