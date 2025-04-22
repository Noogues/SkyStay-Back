package com.example.skystayback.dtos.admin.add;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryAddVO {
    private String name;
    private String iso_code;
    private String continent;
}
