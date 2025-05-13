package com.example.skystayback.dtos.hotel;

import com.example.skystayback.dtos.city.CityVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditHotelVO {

    private String code;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;

}
