package com.example.skystayback.dtos.user;

import com.example.skystayback.enums.RoomType;
import com.example.skystayback.enums.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHotelVO {

    private Integer roomNumber;
    private RoomType roomType;
    private String code;
    private Float amount;
    private Float discount;
    private StatusOrder status;
    private byte[] bill;
}
