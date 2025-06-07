package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDetailVO {
    private Long id;
    private String name;
    private Integer stars;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private String cityName;
    private String countryName;
    private List<String> images;
    private List<RoomDetailsVO> availableRooms;
    private String accommodationType;
    private String amenities;


    public AccommodationDetailVO(Long id, String name, Integer stars, String address, String postalCode,
                                 String phoneNumber, String email, String website, String description,
                                 String cityName, String countryName, String amenities) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.description = description;
        this.cityName = cityName;
        this.countryName = countryName;
        this.amenities = amenities;

    }
}