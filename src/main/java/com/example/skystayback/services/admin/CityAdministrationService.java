package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.city.CityAddVO;
import com.example.skystayback.dtos.city.CityTableVO;
import com.example.skystayback.dtos.city.CityVO;
import com.example.skystayback.dtos.city.CountryAddVO;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.models.City;
import com.example.skystayback.models.CityImage;
import com.example.skystayback.models.Country;
import com.example.skystayback.models.Image;
import com.example.skystayback.repositories.CityRepository;
import com.example.skystayback.repositories.CountryRepository;
import com.example.skystayback.repositories.ImageRepository;
import com.example.skystayback.repositories.CityImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
     * @param cityCountryVOList Lista de objetos `CityAddVO` que contienen los datos de las ciudades y sus países.
     * Cada objeto incluye información sobre el nombre de la ciudad, descripción,
     * población, país y URL de la imagen.
     * @return Un objeto `ResponseVO<String>` que contiene un mensaje indicando el resultado de la operación.
     */

    public ResponseVO<Void> addCityCountry(List<CityAddVO> cityCountryVOList) {
        try {
            for (CityAddVO cityAddVO : cityCountryVOList) {
                Country country = countryRepository.findByNameAndContinent(cityAddVO.getCountry().getName(), cityAddVO.getCountry().getContinent()).orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(cityAddVO.getCountry().getName());
                    newCountry.setIsoCode(cityAddVO.getCountry().getIso_code());
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

            return ResponseVO.<Void>builder().messages(new MessageResponseVO("Paises y ciudades añadidas con exito", 200, LocalDateTime.now())).build();
        } catch (Exception e) {
            System.out.println("addCityCountry: " + e.getMessage());
            return ResponseVO.<Void>builder().messages(new MessageResponseVO("Error al añadir paises y ciudades: " + e.getMessage(), 500, LocalDateTime.now())).build();
        }
    }


    /**
     * Recupera una lista de todas las ciudades de la base de datos.
     * @return Un objeto `ResponseVO<List<CityVO>>` que contiene una lista de objetos `CityVO`
     * representando las ciudades recuperadas y un mensaje indicando el resultado de la operación.
     */
    public ResponsePaginatedVO<CityVO> getCities(PageVO pageVO) {
        try {
            Page<CityVO> cities = cityRepository.getAllCities(pageVO.toPageable());
            ResponsePaginatedVO<CityVO> data = new ResponsePaginatedVO<>();
            data.setObjects(cities.getContent());
            data.setHasNextPage(cities.hasNext());
            data.setHasPreviousPage(cities.hasPrevious());
            data.setCurrentPage(cities.getNumber());
            data.setTotalPages(cities.getTotalPages());
            data.setMessages(new MessageResponseVO("Ciudades recuperadas con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getCities: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las ciudades.", 404, LocalDateTime.now()));
        }
    }

    /**
     * Agrega un nuevo país a la base de datos.
     * @param country Objeto `CountryAddVO` que contiene los datos del país a agregar.
     * @return Un objeto `ResponseVO<String>` que contiene un mensaje indicando el resultado de la operación.
     */
    public ResponseVO<Void> addCountry(CountryAddVO country) {
        try {
            Country newCountry = new Country();
            newCountry.setName(country.getName());
            newCountry.setIsoCode(country.getIso_code());
            newCountry.setContinent(country.getContinent());
            countryRepository.save(newCountry);
            return ResponseVO.<Void>builder().messages(new MessageResponseVO("Pais añadido con exito", 200, LocalDateTime.now())).build();
        } catch (Exception e) {
            System.out.println("addCountry: " + e.getMessage());
            return ResponseVO.<Void>builder().messages(new MessageResponseVO("Error al añadir pais: " + e.getMessage(), 500, LocalDateTime.now())).build();
        }
    }

    public ResponsePaginatedVO<CityTableVO> getCitiesTable(PageVO pageVO) {
        try {
            Page<CityTableVO> cities = cityRepository.getAllCitiesTable(pageVO.toPageable());
            ResponsePaginatedVO<CityTableVO> data = new ResponsePaginatedVO<>();
            data.setObjects(cities.getContent());
            data.setHasNextPage(cities.hasNext());
            data.setHasPreviousPage(cities.hasPrevious());
            data.setCurrentPage(cities.getNumber());
            data.setTotalPages(cities.getTotalPages());
            data.setMessages(new MessageResponseVO("Ciudades recuperadas con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getCitiesTable: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las ciudades.", 404, LocalDateTime.now()));
        }
    }
}
