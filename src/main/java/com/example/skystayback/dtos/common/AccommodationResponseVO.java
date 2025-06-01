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
    private Long hotelId;
    private String hotelName;
    private Integer stars;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private String cityName;
    private List<RoomDetailsVO> availableRooms;

    public AccommodationResponseVO(Long hotelId, String hotelName, Integer stars, String address, String phoneNumber, String email, String website, String description, String cityName) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.stars = stars;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.description = description;
        this.cityName = cityName;
    }
}