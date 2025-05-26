package com.example.skystayback.dtos.airplanes;

import com.example.skystayback.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplanesTypesVO {
    private Long id;
    private String name; // Este campo de aqui es el codigo del avion: Ej. 320, 737,
    private String manufacturer;
    private Integer capacity;
}
