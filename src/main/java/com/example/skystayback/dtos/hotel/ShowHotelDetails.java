package com.example.skystayback.dtos.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowHotelDetails {
    private Long id;
    private String name;
    private String address;
    private String postalCode;
    private String phone_number;
    private String email;
    private String website;
    private Integer starts;
    private String description;

    private String city;
    private String country;
    private String image;

    private List<ShowRoomsDetails> roomsDetails = new ArrayList<>();
}
