package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirplaneForm1VO {

    // Airplane_type:
    private String code;
    private String name; // Este campo de aqui es el codigo del avion: Ej. 320, 737,
    private String manufacturer;
    private Integer capacity;


    // Airplane (necesita airplane_type_id):
    private String model;
    private String registrationNumber;
    private Integer yearOfManufacture;


    // Enumerados de Airplane
    private String type;
    private String status;

}
