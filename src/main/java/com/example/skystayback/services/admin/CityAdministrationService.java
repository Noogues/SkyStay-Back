package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.city.CityAddVO;
import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.models.City;
import com.example.skystayback.models.CityImage;
import com.example.skystayback.models.Country;
import com.example.skystayback.models.Image;
import com.example.skystayback.repositories.CityRepository;
import com.example.skystayback.repositories.CountryRepository;
import com.example.skystayback.repositories.ImageRepository;
import com.example.skystayback.repositories.CityImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CityAdministrationService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ImageRepository imageRepository;
    private final CityImageRepository cityImageRepository;

    /**
     * Agrega una lista de ciudades y sus países asociados a la base de datos.
     * Si un país ya existe con los mismos datos, no se crea uno nuevo.
     *
     * @param cityCountryVOList Lista de objetos `CityAddVO` que contienen los datos de las ciudades y sus países.
     *                          Cada objeto incluye información sobre el nombre de la ciudad, descripción,
     *                          población, país y URL de la imagen.
     * @return Un objeto `ResponseVO<String>` que contiene un mensaje indicando el resultado de la operación.
     */

    public ResponseVO<String> addCityCountry(List<CityAddVO> cityCountryVOList) {
        try {
            for (CityAddVO cityAddVO : cityCountryVOList) {
                Country country = countryRepository.findByNameAndContinent(
                        cityAddVO.getCountry().getName(),
                        cityAddVO.getCountry().getContinent()
                ).orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(cityAddVO.getCountry().getName());
                    newCountry.setIso_code(cityAddVO.getCountry().getIso_code());
                    newCountry.setContinent(cityAddVO.getCountry().getContinent());
                    return countryRepository.save(newCountry);
                });

                City city = new City();
                city.setName(cityAddVO.getName());
                city.setDescription(cityAddVO.getDescription());
                city.setPopulation(cityAddVO.getPopulation());
                city.setCountry(country);
                city = cityRepository.save(city);

                Image image = new Image();
                image.setUrl(cityAddVO.getImage());
                image = imageRepository.save(image);

                CityImage cityImage = new CityImage();
                cityImage.setCity(city);
                cityImage.setImage(image);
                cityImageRepository.save(cityImage);
            }

            return ResponseVO.<String>builder()
                    .messages(new MessageResponseVO("Paises y ciudades añadidas con exito", 200, LocalDateTime.now()))
                    .build();
        } catch (Exception e) {
            return ResponseVO.<String>builder()
                    .messages(new MessageResponseVO("Error al añadir paises y ciudades: " + e.getMessage(), 500, LocalDateTime.now()))
                    .build();
        }
    }

    public ResponseVO <List<CityVO>> getCities() {
        try {
            List<CityVO> cities = cityRepository.getAllCities();
            return new ResponseVO<>(new DataVO<>(cities), new MessageResponseVO("Ciudades recuperadas con éxito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(new ArrayList<>()), new MessageResponseVO("Error al recuperar las ciudades:", 404, LocalDateTime.now()));
        }
    }
}
