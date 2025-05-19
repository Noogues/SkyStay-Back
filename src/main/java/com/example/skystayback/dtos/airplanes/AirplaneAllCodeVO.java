package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneAllCodeVO {
    private String model;
    private String registrationNumber;
    private Integer yearOfManufacture;
    private AirplaneTypeEnum type;
    private Status status;

    private String airplaneType_code;
    private String airplaneType_manufacturer;
    private Integer airplaneType_capacity;

}
