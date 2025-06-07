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
    private Long id;
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

    public AccommodationResponseVO(Long id, String name, Integer stars, String address, String phoneNumber, String email, String website, String description, String cityName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.description = description;
        this.cityName = cityName;
        this.img = imageUrl;
    }
}