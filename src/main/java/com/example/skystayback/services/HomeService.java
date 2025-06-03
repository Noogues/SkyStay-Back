package com.example.skystayback.services;

import com.example.skystayback.dtos.city.CityImageVO;
import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.repositories.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class HomeService {

    final CityRepository cityRepository;


    /**
     * Recupera una lista de 8 ciudades con sus imagenes, ordenadas aleatoriamente.
     *
     * @return ResponseVO con una lista de CityImageVO y un mensaje de éxito o error.
     */
    public ResponseVO<List<CityImageVO>> listFiveCitys() {
        try {
            List<CityImageVO> list = cityRepository.allCitiesImages().stream().limit(6).sorted((a, b) -> Math.random() > 0.5 ? 1 : -1).toList();
            return new ResponseVO<>(new DataVO<>(list), new MessageResponseVO("La lista de ciudades con sus imagenes recuperadas con exito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("listFiveCitys: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al intentar recuperar la lista de ciudades con sus imagenes", 500, LocalDateTime.now()));
        }
    }


}
