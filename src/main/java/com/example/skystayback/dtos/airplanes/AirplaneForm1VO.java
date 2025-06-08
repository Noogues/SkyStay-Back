package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirplaneForm1VO {

    // Airplane_type:
    private Long airplane_type_id;

    private Long airline_id;

    // Airplane (necesita airplane_type_id):
    private String model;
    private String registrationNumber;
    private Integer yearOfManufacture;


    // Enumerados de Airplane
    private String type;
    private String status;

}
