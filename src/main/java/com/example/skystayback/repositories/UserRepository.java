package com.example.skystayback.repositories;

import com.example.skystayback.dtos.user.*;
import com.example.skystayback.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findTopByEmail(String email);
    Optional<User> findFirstByEmail(String email);
    boolean existsByUserCode(String userCode);
    boolean existsByEmail(String email);
    boolean existsByNif(String nif);
    boolean existsByPhone(String phone);

    User getUserByUserCode(String userCode);

    //CONSULTAS DE LA ADMINISTRACION
    @Query("SELECT new com.example.skystayback.dtos.user.UserAdminVO(u.userCode, u.name, u.lastName, u.email, u.nif, u.phone, u.rol) FROM User u")
    Page<UserAdminVO> getAllUsers(Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.user.AirlineRatingVO(a.name, ar.rating) FROM Airline a INNER JOIN AirlineRating ar ON ar.airline.id = a.id WHERE ar.user.userCode = :userCode")
    Page<AirlineRatingVO> findAllAirlineRatingByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.user.ApartmentRatingVO(a.name, ar.rating) FROM Apartment a LEFT JOIN ApartmentRating ar ON ar.apartment.id = a.id WHERE ar.user.userCode = :userCode")
    Page<ApartmentRatingVO> findAllApartmentRatingByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.user.HotelRatingVO(h.name, hr.rating) FROM Hotel h LEFT JOIN HotelRating hr ON hr.hotel.id = h.id WHERE hr.user.userCode = :userCode")
    Page<HotelRatingVO> findAllHotelRatingByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.user.OrderApartmentVO(oa.roomBooking.room.roomConfiguration.apartment.name, oa.code, oa.amount, oa.discount, oa.status, oa.bill) " +
            "FROM OrderApartment oa WHERE oa.roomBooking.user.userCode = :userCode")
    Page<OrderApartmentVO> findAllOrderApartmentByUserId(@Param("userCode") String userCode, Pageable pageable);

//    @Query("SELECT new com.example.skystayback.dtos.user.OrderFlightVO(o.seatBooking.flightSeatStatus.flight.code, o.code, o.amount, o.discount, o.status, o.bill) " +
//            "FROM OrderFlight o WHERE o..user.userCode = :userCode")
//    Page<OrderFlightVO> findAllOrderFlightByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.user.OrderHotelVO(r.roomNumber, oh.roomBooking.room.roomConfiguration.roomConfiguration.type, oh.code, oh.amount, oh.discount, oh.status, oh.bill) " +
            "FROM OrderHotel oh JOIN oh.roomBooking.room r " +
            "WHERE oh.roomBooking.user.userCode = :userCode")
    Page<OrderHotelVO> findAllOrderHotelByUserId(@Param("userCode") String userCode, Pageable pageable);


    @Query("SELECT new com.example.skystayback.dtos.user.UserAdminVO(u.userCode, u.name, u.lastName, u.email, u.nif, u.phone, u.rol) " +
            "FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<UserAdminVO> findByNameOrLastNameContainingIgnoreCase(@Param("search") String search, Pageable pageable);

    
    Optional<User> findTopByEmailAndCode(String email, Integer code);

    @Query("SELECT new com.example.skystayback.dtos.user.UserAdminVO(u.userCode, u.name, u.lastName, u.email, u.nif, u.phone, u.rol) " +
            "FROM User u ORDER BY u.id DESC")
    List<UserAdminVO> getLast5User();
}

