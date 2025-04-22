package com.example.skystayback.dtos.admin.all;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelAdminVO {

    private String code;
    private String name;
    private String address;
    private String postal_code;
    private String phone_number;
    private String email;
    private String website;
    private String description;
    private Integer stars;
    private String image;
    private CityVO city;


    public HotelAdminVO(String code, String name, String address, String postal_code, String phone_number, String email, String website, Integer stars, String description, String image, CityVO city) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.postal_code = postal_code;
        this.phone_number = phone_number;
        this.email = email;
        this.website = website;
        this.stars = stars;
        this.description = description;
        this.image = image;
        this.city = city;
    }
}
