package com.example.skystayback.dtos.admin.add;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelApartmentsAddVO {
    private String name;
    private String address;
    private String postalCode;
    private String phone_number;
    private String email;
    private String website;
    private String description;
    private Long cityId;
}
