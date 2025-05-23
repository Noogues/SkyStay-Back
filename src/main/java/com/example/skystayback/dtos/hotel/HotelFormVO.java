package com.example.skystayback.dtos.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelFormVO {
    private String name;
    private String address;
    private String postalCode;
    private String phone_number;
    private String email;
    private String website;
    private String description;
    private Long cityId;
    private List<RoomFormVO> rooms;
}
