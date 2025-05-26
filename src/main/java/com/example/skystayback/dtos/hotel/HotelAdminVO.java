package com.example.skystayback.dtos.hotel;

import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.models.Hotel;
import com.example.skystayback.models.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelAdminVO {

    private String code;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private Integer stars;
    private String url;
    private CityVO city;

}
