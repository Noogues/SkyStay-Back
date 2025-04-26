package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneShowVO {

    private String code;
    private String model;
    private String registrationNumber;
    private Integer yearOfManufacture;
    private AirplaneTypeEnum type;
    private Status status;
    private AirplaneTypeVO airplaneType;

}
