package com.example.skystayback.dtos.hotel;


import com.example.skystayback.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomConfigurationVO {

    private Long id;
    private Integer capacity;
    private RoomType type;
    private String url;
}
