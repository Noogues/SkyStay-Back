package com.example.skystayback.dtos.common;

import com.example.skystayback.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationResponseVO {
    private String code;
    private String name;
    private Integer stars;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private String cityName;
    private List<RoomDetailsVO> availableRooms;
    private String img;
    private String accommodationType;
    private String amenities;
    private Double averageRating;

    public AccommodationResponseVO(String code, String name, Integer stars, String address, String phoneNumber, String email, String website, String description, String cityName, String imageUrl, String amenities, Double averageRating) {
        this.code = code;
        this.name = name;
        this.stars = stars;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.description = description;
        this.cityName = cityName;
        this.img = imageUrl;
        this.amenities = amenities;
        this.averageRating = averageRating;
    }
}