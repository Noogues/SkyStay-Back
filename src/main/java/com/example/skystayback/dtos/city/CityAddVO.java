package com.example.skystayback.dtos.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityAddVO {

    private String name;
    private String description;
    private String population;
    private String image;
    private CountryAddVO country;
}
