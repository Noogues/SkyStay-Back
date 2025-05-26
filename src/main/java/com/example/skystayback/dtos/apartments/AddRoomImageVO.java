package com.example.skystayback.dtos.apartments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomImageVO {
    private String apartmentCode;
    private String url;
    private String roomType;
}
