package com.example.skystayback.services.admin;


import com.example.skystayback.dtos.airplanes.*;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.enums.AirplaneTypeEnum;
import com.example.skystayback.enums.SeatClass;
import com.example.skystayback.enums.Status;
import com.example.skystayback.models.*;
import com.example.skystayback.repositories.*;
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
    private final SeatRepository seatRepository;
    private final ImageRepository imageRepository;
    private final AirplaneImageRepository airplaneImageRepository;

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
            String code;
            do {
                code = userService.generateShortUuid();
            } while (airplaneRepository.findByCode(code).isPresent());

            airplane.setCode(code);
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

    /**
     * Crea un avión en la base de datos.
     * @param form datos del avión a crear.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */

    public ResponseVO<Void> createAirplanePart2(List<AirplaneForm2VO> form) {
        try {
            System.out.println("Formulario de creación de avión: " + form);
            for (AirplaneForm2VO af : form) {
                System.out.println("Procesando cabina: " + af);

                // Buscar la configuración de asientos
                SeatConfiguration seatConfiguration = seatConfigurationRepository.findById(af.getSeat_configuration_id()).orElseThrow(() -> new IllegalArgumentException("No se encontró la configuración de asiento con el ID proporcionado"));

                // Crear la cabina
                AirplaneCabin airplaneCabin = new AirplaneCabin();
                airplaneCabin.setSeatClass(SeatClass.valueOf(af.getSeat_class()));
                airplaneCabin.setRowStart(af.getRowStart());
                airplaneCabin.setRowEnd(af.getRowEnd());

                // Buscar el avión
                Airplane plane = airplaneRepository.findById(af.getAirplane_id()).orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el avión con ese ID"));

                airplaneCabin.setAirplane(plane);
                airplaneCabin.setSeatConfiguration(seatConfiguration);

                // Guardar la cabina
                airplaneCabin = airplaneCabinRepository.save(airplaneCabin);

                // Crear los asientos para la cabina
                String pattern = seatConfiguration.getSeatPattern();
                String[] blocks = pattern.split(" ");
                int totalSeats = 0;

                for (int row = af.getRowStart(); row <= af.getRowEnd(); row++) {
                    for (String block : blocks) {
                        String[] seats = block.split("-");
                        for (String seatLetter : seats) {
                            Seat seat = new Seat();
                            seat.setSeatRow(String.valueOf(row));
                            seat.setSeatColumn(seatLetter);
                            seat.setState(true);
                            seat.setCabin(airplaneCabin);
                            seatRepository.save(seat);
                            totalSeats++;
                        }
                    }
                }

                // Validar si se alcanzó la capacidad del avión
                if (totalSeats < plane.getAirplaneType().getCapacity() && af.equals(form.get(form.size() - 1))) {
                    throw new IllegalArgumentException("El número total de asientos creados no alcanza la capacidad del avión.");
                }
            }

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Cabinas y asientos creados correctamente.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear las cabinas y asientos.", 404, LocalDateTime.now()));
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
    public ResponseVO<List<SeatConfigurationVO>> getAllSeatConfiguration() {
        try {
            List<SeatConfiguration> list = seatConfigurationRepository.findAll();
            List<SeatConfigurationVO> finalist = new ArrayList<>();

            for (SeatConfiguration sc : list) {
                SeatConfigurationVO seatConfigurationVO = new SeatConfigurationVO();
                seatConfigurationVO.setId(sc.getId());
                seatConfigurationVO.setSeatPattern(sc.getSeatPattern());
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
        try {
            List<AirplaneType> airplaneTypes = airplaneTypeRepository.findAll();
            List<AirplanesTypesVO> list = new ArrayList<>();

            for (AirplaneType airplaneType : airplaneTypes) {
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
            seatConfigurationEntity.setSeatPattern(seatConfiguration.getSeatPattern());
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

            String code;
            do {
                code = userService.generateShortUuid();
            } while (airplaneTypeRepository.findByCode(code).isPresent());

            airplaneType.setCode(code);
            airplaneType.setName(airplanesTypes.getName());
            airplaneType.setManufacturer(airplanesTypes.getManufacturer());
            airplaneType.setCapacity(airplanesTypes.getCapacity());
            airplaneTypeRepository.save(airplaneType);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Tipo de avión creado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al crear el tipo de avión", 404, LocalDateTime.now()));
        }
    }

    /**
     * Obtiene la información básica de un avión por su código (Informacion del tipo de avion, datos de este...).
     * @param airplaneCode código del avión.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<AirplaneAllCodeVO> getBasicInfoByCode(String airplaneCode) {
        try {
            AirplaneAllCodeVO airplane = airplaneRepository.findBasicInfoByCode(airplaneCode);
            return new ResponseVO<>(new DataVO<>(airplane), new MessageResponseVO("Información básica del avión recuperada con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar la información básica del avión.", 404, LocalDateTime.now()));
        }
    }


    /**
     * Obtiene todas las cabinas (toda la informacion en cuantos a estos) y todos los asientos que hay de un avión por su código.
     * @param airplaneCode código del avión.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<List<CabinWithSeatsVO>> getCabinsWithSeatsByAirplaneCode(String airplaneCode) {
        try {

            List<AirplaneCabin> cabins = airplaneCabinRepository.findAllByAirplaneCode(airplaneCode);
            List<CabinWithSeatsVO> cabinsWithSeats = new ArrayList<>();

            for (AirplaneCabin cabin : cabins) {
                List<Seat> cabinSeats = seatRepository.findByCabinId(cabin.getId());
                List<SeatVO> seatVOs = new ArrayList<>();

                for (Seat seat : cabinSeats) {
                    seatVOs.add(new SeatVO(seat.getId(), seat.getSeatRow(), seat.getSeatColumn(), seat.getState()));
                }

                CabinWithSeatsVO cabinWithSeats = new CabinWithSeatsVO(cabin.getId(), cabin.getSeatConfiguration().getSeatPattern(), cabin.getRowStart(), cabin.getRowEnd(), cabin.getSeatClass(), seatVOs);

                cabinsWithSeats.add(cabinWithSeats);
            }

            return new ResponseVO<>(new DataVO<>(cabinsWithSeats), new MessageResponseVO("Cabinas y asientos del avión recuperados con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las cabinas y asientos del avión.", 404, LocalDateTime.now()));
        }
    }


    /**
     * Actualiza el estado de un avión en la base de datos.
     * @param airplaneStatus datos del avión a actualizar.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<Void> updateAirplaneStatus(UpdateAirplaneStatusVO airplaneStatus) {
        try {
            Airplane airplane = airplaneRepository.findByCode(airplaneStatus.getAirplaneCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el avión con el codigo del avion proporcionado"));

            airplane.setStatus(Status.valueOf(airplaneStatus.getStatus()));
            airplaneRepository.save(airplane);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Estado del avión actualizado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al actualizar el estado del avión.", 404, LocalDateTime.now()));
        }
    }

    /**
     * Añade una imagen a un avión en la base de datos.
     * @param airplaneImage datos de la imagen a añadir.
     * @return response que devuelve los datos y un mensaje de éxito o error.
     */
    public ResponseVO<Void> addAirplaneImage(AddImageVO airplaneImage) {
        try {
            Airplane airplane = airplaneRepository.findByCode(airplaneImage.getCode()).orElseThrow(() -> new IllegalArgumentException("No se encontró el avión con el código proporcionado"));

            Image image = new Image();
            image.setUrl(airplaneImage.getImage());
            image = imageRepository.save(image);

            AirplaneImage airplaneImage1 = new AirplaneImage();
            airplaneImage1.setAirplane(airplane);
            airplaneImage1.setImage(image);
            airplaneImageRepository.save(airplaneImage1);

            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Imagen del avión añadida con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al añadir la imagen del avión.", 404, LocalDateTime.now()));
        }
    }
}
