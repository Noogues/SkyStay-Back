package com.example.skystayback.services.admin;


import com.example.skystayback.dtos.airplanes.*;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.SeatClass;
import com.example.skystayback.enums.Status;
import com.example.skystayback.models.Airplane;
import com.example.skystayback.models.AirplaneCabin;
import com.example.skystayback.models.AirplaneType;
import com.example.skystayback.models.SeatConfiguration;
import com.example.skystayback.repositories.AirplaneCabinRepository;
import com.example.skystayback.repositories.AirplaneRepository;
import com.example.skystayback.repositories.AirplaneTypeRepository;
import com.example.skystayback.repositories.SeatConfigurationRepository;
import com.example.skystayback.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AirplaneAdministrationService {

    private final AirplaneRepository airplaneRepository;
    private final AirplaneTypeRepository airplaneTypeRepository;
    private final UserService userService;
    private final SeatConfigurationRepository seatConfigurationRepository;
    private final AirplaneCabinRepository airplaneCabinRepository;

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
    public ResponseVO<Long> createAirplanePart1(AirplaneForm1VO form) {
        try {
            AirplaneType at = airplaneTypeRepository.findById(form.getAirplane_type_id()).orElseThrow(() -> new IllegalArgumentException("No se encontró el tipo de avión con el Id proporcionado"));

            Airplane airplane = new Airplane();
            airplane.setCode(userService.generateShortUuid());
            airplane.setModel(form.getModel());
            airplane.setRegistrationNumber(form.getRegistrationNumber());
            airplane.setYearOfManufacture(form.getYearOfManufacture());
            airplane.setStatus(Status.valueOf(form.getStatus()));
            airplane.setType(AirplaneTypeEnum.valueOf(form.getType()));

            airplane.setAirplaneType(at);
            airplane = airplaneRepository.save(airplane);
            return new ResponseVO<>(new DataVO<>(airplane.getId()), new MessageResponseVO("Tipo de avión y avión creados con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear el tipo de avión y el avión", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<Void> createAirplanePart2(List<AirplaneForm2VO> form) {
        try {
            for (AirplaneForm2VO af : form) {
                SeatConfiguration seatConfiguration = seatConfigurationRepository.findById(af.getSeat_configuration_id()).orElseThrow(() -> new IllegalArgumentException("No se encontró la configuración de asiento con el ID proporcionado"));

                AirplaneCabin airplaneCabin = new AirplaneCabin();
                airplaneCabin.setRowStart(af.getRowStart());
                airplaneCabin.setRowEnd(af.getRowEnd());

                Airplane plane = airplaneRepository.findById(af.getAirplane_id())
                        .orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el avión con ese id"));

                airplaneCabin.setAirplane(plane);
                airplaneCabin.setSeatConfiguration(seatConfiguration);

                airplaneCabinRepository.save(airplaneCabin);

            }

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Asientos, cabinas e imagen creadas y asignadas correctamente al avion ", 200, LocalDateTime.now()));
        }catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Se ha producido un error al crear lo asientos y cabinas.", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene todas las clases de asiento del enumerado.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<List<SeatClass>> getAllSeatClassEnum() {
        try {
            List<SeatClass> seatClassEnumList = List.of(SeatClass.values());
            return new ResponseVO<>(new DataVO<>(seatClassEnumList), new MessageResponseVO("Clases de asientos recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las clases de asientos", 404, LocalDateTime.now()));
        }
    }


    /**
     * Obtiene todas las configuraciones de asientos de la base de datos.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<List<SeatConfigurationVO>> getAllSeatConfiguration(){
        try {
            List<SeatConfiguration> list = seatConfigurationRepository.findAll();
            List<SeatConfigurationVO> finalist = new ArrayList<>();

            for (SeatConfiguration sc : list) {
                SeatConfigurationVO seatConfigurationVO = new SeatConfigurationVO();
                seatConfigurationVO.setId(sc.getId());
                seatConfigurationVO.setSeatClass(sc.getSeatClass());
                seatConfigurationVO.setSeatPattern(sc.getSeatPattern());
                seatConfigurationVO.setTotalRows(sc.getTotalRows());
                finalist.add(seatConfigurationVO);
            }
            return new ResponseVO<>(new DataVO<>(finalist), new MessageResponseVO("Configuraciones de asientos recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las configuraciones de asientos", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene todos los tipos de avión de la base de datos.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<List<AirplanesTypesVO>> getAllAirplanesTypes() {
        try{
            List<AirplaneType> airplaneTypes = airplaneTypeRepository.findAll();
            List<AirplanesTypesVO> list = new ArrayList<>();

            for(AirplaneType airplaneType : airplaneTypes) {
                AirplanesTypesVO airplanesTypesVO = new AirplanesTypesVO();
                airplanesTypesVO.setId(airplaneType.getId());
                airplanesTypesVO.setName(airplaneType.getName());
                airplanesTypesVO.setManufacturer(airplaneType.getManufacturer());
                airplanesTypesVO.setCapacity(airplaneType.getCapacity());
                list.add(airplanesTypesVO);
            }

            return new ResponseVO<>(new DataVO<>(list), new MessageResponseVO("Tipos de aviones recuperados con exito", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar los tipos de aviones", 404, LocalDateTime.now()));
        }
    }

    /**
     * Crea una configuración de asientos en la base de datos.
     * @param seatConfiguration datos de la configuración de asientos a crear.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */

    public ResponseVO<Void> createSeatConfiguration(CreateSeatConfigurationVO seatConfiguration) {
        try {
            SeatConfiguration seatConfigurationEntity = new SeatConfiguration();
            seatConfigurationEntity.setSeatClass(SeatClass.valueOf(String.valueOf(seatConfiguration.getSeatClass())));
            seatConfigurationEntity.setSeatPattern(seatConfiguration.getSeatPattern());
            seatConfigurationEntity.setTotalRows(seatConfiguration.getTotalRows());
            seatConfigurationEntity.setDescription(seatConfiguration.getDescription());
            seatConfigurationRepository.save(seatConfigurationEntity);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Configuración de asientos creada con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear la configuración de asientos", 404, LocalDateTime.now()));
        }
    }

    /**
     * Crea un tipo de avión en la base de datos.
     * @param airplanesTypes datos del tipo de avión a crear.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<Void> createAirplanesTypes(CreateAirplanesTypesVO airplanesTypes) {
        try {
            AirplaneType airplaneType = new AirplaneType();
            airplaneType.setCode(userService.generateShortUuid());
            airplaneType.setName(airplanesTypes.getName());
            airplaneType.setManufacturer(airplanesTypes.getManufacturer());
            airplaneType.setCapacity(airplanesTypes.getCapacity());
            airplaneTypeRepository.save(airplaneType);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Tipo de avión creado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear el tipo de avión", 404, LocalDateTime.now()));
        }
    }
}
