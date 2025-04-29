package com.example.skystayback.services.admin;


import com.example.skystayback.dtos.airplanes.AirplaneForm1VO;
import com.example.skystayback.dtos.airplanes.AirplaneForm2VO;
import com.example.skystayback.dtos.airplanes.AirplaneShowVO;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.Status;
import com.example.skystayback.models.Airplane;
import com.example.skystayback.models.AirplaneType;
import com.example.skystayback.repositories.AirplaneRepository;
import com.example.skystayback.repositories.AirplaneTypeRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AirplaneAdministrationService {

    private final AirplaneRepository airplaneRepository;
    private final AirplaneTypeRepository airplaneTypeRepository;
    private final UserService userService;

    /**
     * Obtiene todos los aviones de la base de datosm paginados dependiendo de los parámetros de la clase PageVO que se le pase.
     * @param pageVO parametros de paginación (limit y page)
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponsePaginatedVO<AirplaneShowVO> getAllAirplanes(PageVO pageVO) {
        try {
            Page<AirplaneShowVO> airplanePage = airplaneRepository.getAllAirplanes(pageVO.toPageable());
            ResponsePaginatedVO<AirplaneShowVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airplanePage.getContent());
            data.setHasNextPage(airplanePage.hasNext());
            data.setHasPreviousPage(airplanePage.hasPrevious());
            data.setCurrentPage(airplanePage.getNumber());
            data.setTotalPages(airplanePage.getTotalPages());
            data.setMessages(new MessageResponseVO("Aviones recuperados con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los aviones", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene todos los tipos de avión de la base de datos.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<List<AirplaneTypeEnum>> getAllAirplaneTypeEnum() {
        try {
            List<AirplaneTypeEnum> airplaneTypeEnumList = List.of(AirplaneTypeEnum.values());
            return new ResponseVO<>(new DataVO<>(airplaneTypeEnumList), new MessageResponseVO("Tipos de aviones recuperados con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar los tipos de aviones", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene todos los estados de avión de la base de datos.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<List<Status>> getAllStatusEnum() {
        try {
            List<Status> statusEnumList = List.of(Status.values());
            return new ResponseVO<>(new DataVO<>(statusEnumList), new MessageResponseVO("Estados de aviones recuperados con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar los estados de los aviones", 404, LocalDateTime.now()));
        }
    }

    /**
     * Crea un avión en la base de datos.
     * @param form datos del avión a crear.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<Void> createAirplanePart1(AirplaneForm1VO form) {
        try {
            AirplaneType airplaneType = new AirplaneType();
            airplaneType.setCode(userService.generateShortUuid());
            airplaneType.setName(form.getName());
            airplaneType.setManufacturer(form.getManufacturer());
            airplaneType.setCapacity(form.getCapacity());
            airplaneTypeRepository.save(airplaneType);

            Airplane airplane = new Airplane();
            airplane.setCode(userService.generateShortUuid());
            airplane.setModel(form.getModel());
            airplane.setRegistrationNumber(form.getRegistrationNumber());
            airplane.setYearOfManufacture(form.getYearOfManufacture());
            airplane.setStatus(Status.valueOf(form.getStatus()));
            airplane.setType(AirplaneTypeEnum.valueOf(form.getType()));

            airplane.setAirplaneType(airplaneType);
            airplaneRepository.save(airplane);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Tipo de avión y avión creados con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear el tipo de avión y el avión", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<Void> createAirplanePart2(AirplaneForm2VO form) {
        try {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Asientos, cabinas e imagen creadas y asignadas correctamente al avion ", 200, LocalDateTime.now()));
        }catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Se ha producido un error al crear lo asientos y cabinas.", 404, LocalDateTime.now()));
        }
    }




}
