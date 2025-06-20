package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestinationVO {
    private String code;
    private String name;
    private String image;
}