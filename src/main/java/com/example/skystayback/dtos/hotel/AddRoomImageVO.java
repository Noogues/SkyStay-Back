package com.example.skystayback.dtos.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomImageVO {
    private String hotelCode;
    private String url;
    private String roomType;
}
