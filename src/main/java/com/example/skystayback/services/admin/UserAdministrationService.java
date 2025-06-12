package com.example.skystayback.services.admin;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.user.*;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserAdministrationService {

    private final UserRepository userRepository;

   /**
     * Metodo para obtener todos los usuarios
     * @param pageVO informacion de paginacion
     * @param search cadena de busqueda
     * @return lista de usuarios
     */
    public ResponsePaginatedVO<UserAdminVO> getUsers(PageVO pageVO, String search) {
        try {
            Page<UserAdminVO> users;
            if (search != null && !search.isEmpty()) {
                users = userRepository.findByNameOrLastNameContainingIgnoreCase(search, pageVO.toPageable());
            }else {
                users = userRepository.getAllUsers(pageVO.toPageable());
            }
            ResponsePaginatedVO<UserAdminVO> data = new ResponsePaginatedVO<>();
            data.setObjects(users.getContent());
            data.setHasNextPage(users.hasNext());
            data.setHasPreviousPage(users.hasPrevious());
            data.setCurrentPage(users.getNumber());
            data.setTotalPages(users.getTotalPages());
            data.setMessages(new MessageResponseVO("Usuarios recuperados con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getUsers: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los usuarios:", 404, LocalDateTime.now()));
        }
    }
    /**
     * Metodo para obtener las calificaciones de aerolíneas mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de calificaciones de aerolíneas
     */
    public ResponsePaginatedVO<AirlineRatingVO> getAirlaneRating(String userCode, PageVO pageVO) {
        try {
            Page<AirlineRatingVO> airlineRating = userRepository.findAllAirlineRatingByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<AirlineRatingVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airlineRating.getContent());
            data.setHasNextPage(airlineRating.hasNext());
            data.setHasPreviousPage(airlineRating.hasPrevious());
            data.setCurrentPage(airlineRating.getNumber());
            data.setTotalPages(airlineRating.getTotalPages());
            data.setMessages(new MessageResponseVO("Calificaciones de aerolíneas recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAirlaneRating: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las calificaciones de aerolíneas:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las calificaciones de apartamentos mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de calificaciones de apartamentos
     */
    public ResponsePaginatedVO<ApartmentRatingVO> getApartmentRating(String userCode, PageVO pageVO) {
        try {
            Page<ApartmentRatingVO> apartmentRating = userRepository.findAllApartmentRatingByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<ApartmentRatingVO> data = new ResponsePaginatedVO<>();
            data.setObjects(apartmentRating.getContent());
            data.setHasNextPage(apartmentRating.hasNext());
            data.setHasPreviousPage(apartmentRating.hasPrevious());
            data.setCurrentPage(apartmentRating.getNumber());
            data.setTotalPages(apartmentRating.getTotalPages());
            data.setMessages(new MessageResponseVO("Calificaciones de apartamentos recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getApartmentRating: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las calificaciones de apartamentos:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las calificaciones de hoteles mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de calificaciones de hoteles
     */
    public ResponsePaginatedVO<HotelRatingVO> getHotelRating(String userCode, PageVO pageVO) {
        try {
            Page<HotelRatingVO> hotelRating = userRepository.findAllHotelRatingByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<HotelRatingVO> data = new ResponsePaginatedVO<>();
            data.setObjects(hotelRating.getContent());
            data.setHasNextPage(hotelRating.hasNext());
            data.setHasPreviousPage(hotelRating.hasPrevious());
            data.setCurrentPage(hotelRating.getNumber());
            data.setTotalPages(hotelRating.getTotalPages());
            data.setMessages(new MessageResponseVO("Calificaciones de hoteles recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getHotelRating: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las calificaciones de hoteles:", 404, LocalDateTime.now()));
        }
    }

    /**
     * Metodo para obtener las ordenes de apartamentos mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de ordenes de apartamentos
     */
    public ResponsePaginatedVO<OrderApartmentVO> getOrderApartment(String userCode, PageVO pageVO) {
        try {
            Page<OrderApartmentVO> orderApartment = userRepository.findAllOrderApartmentByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<OrderApartmentVO> data = new ResponsePaginatedVO<>();
            data.setObjects(orderApartment.getContent());
            data.setHasNextPage(orderApartment.hasNext());
            data.setHasPreviousPage(orderApartment.hasPrevious());
            data.setCurrentPage(orderApartment.getNumber());
            data.setTotalPages(orderApartment.getTotalPages());
            data.setMessages(new MessageResponseVO("Órdenes de apartamentos recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getOrderApartment: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las órdenes de apartamentos:", 404, LocalDateTime.now()));
        }
    }

//    /**
//     * Metodo para obtener las ordenes de vuelos mediante el codigo del usuario
//     * @param userCode codigo del usuario
//     * @param pageVO informacion de paginacion
//     * @return lista de ordenes de vuelos
//     */
//    public ResponsePaginatedVO<OrderFlightVO> getOrderFlight(String userCode, PageVO pageVO) {
//        try {
//            Page<OrderFlightVO> orderFlight = userRepository.findAllOrderFlightByUserId(userCode, pageVO.toPageable());
//            ResponsePaginatedVO<OrderFlightVO> data = new ResponsePaginatedVO<>();
//            data.setObjects(orderFlight.getContent());
//            data.setHasNextPage(orderFlight.hasNext());
//            data.setHasPreviousPage(orderFlight.hasPrevious());
//            data.setCurrentPage(orderFlight.getNumber());
//            data.setTotalPages(orderFlight.getTotalPages());
//            data.setMessages(new MessageResponseVO("Órdenes de vuelos recuperadas con éxito", 200, LocalDateTime.now()));
//            return data;
//        } catch (Exception e) {
//            System.out.println("getOrderFlight: " + e.getMessage());
//            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las órdenes de vuelos:", 404, LocalDateTime.now()));
//        }
//    }

    /**
     * Metodo para obtener las ordenes de hoteles mediante el codigo del usuario
     * @param userCode codigo del usuario
     * @param pageVO informacion de paginacion
     * @return lista de ordenes de hoteles
     */
    public ResponsePaginatedVO<OrderHotelVO> getOrderHotel(String userCode, PageVO pageVO) {
        try {
            Page<OrderHotelVO> orderHotel = userRepository.findAllOrderHotelByUserId(userCode, pageVO.toPageable());
            ResponsePaginatedVO<OrderHotelVO> data = new ResponsePaginatedVO<>();
            data.setObjects(orderHotel.getContent());
            data.setHasNextPage(orderHotel.hasNext());
            data.setHasPreviousPage(orderHotel.hasPrevious());
            data.setCurrentPage(orderHotel.getNumber());
            data.setTotalPages(orderHotel.getTotalPages());
            data.setMessages(new MessageResponseVO("Órdenes de hoteles recuperadas con éxito", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getOrderHotel: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar las órdenes de hoteles:", 404, LocalDateTime.now()));
        }
    }

    public ResponseVO<UserInfoVO> getUserInfoByCode(String userCode){
        try{
            User user = userRepository.getUserByUserCode(userCode);
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setName(user.getName() + " " + user.getLastName());
            // Quitar ROLE_ y ademas dejar solo en mayuscula la primera letra.
            String rol = user.getRol().toString().toLowerCase().split("_")[1];
            rol = Character.toUpperCase(rol.charAt(0)) + rol.substring(1);
            userInfoVO.setRole(rol);
            return ResponseVO.<UserInfoVO>builder()
                    .response(new DataVO<>(userInfoVO))
                    .messages(new MessageResponseVO("Información del usuario obtenida", 200, LocalDateTime.now()))
                    .build();
        }catch (Exception e){
            System.out.println("getUserInfoByCode: " + e.getMessage());
            return new ResponseVO<>(null, new MessageResponseVO("Error al intentar obtener la información del usuario", 400, LocalDateTime.now()));
        }
    }
}
