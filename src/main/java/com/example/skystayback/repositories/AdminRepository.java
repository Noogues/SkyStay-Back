package com.example.skystayback.repositories;
import com.example.skystayback.dtos.admin.all.*;
import com.example.skystayback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<User, Long> {

    //Consulta para obtener todos los usuarios.
    @Query("SELECT new com.example.skystayback.dtos.admin.all.UserAdminVO(u.userCode, u.name, u.lastName, u.email, u.nif, u.phone, u.rol) FROM User u")
    Page<UserAdminVO> getAllUsers(Pageable pageable);

    // Consultas para obtener todo lo relacionado con los usuarios
    @Query("SELECT new com.example.skystayback.dtos.admin.all.AirlineRatingVO(a.name, ar.rating) FROM Airline a INNER JOIN AirlineRating ar ON ar.airline.id = a.id WHERE ar.user.userCode = :userCode")
    Page<AirlineRatingVO> findAllAirlineRatingByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.ApartmentRatingVO(a.name, ar.rating) FROM Apartment a LEFT JOIN ApartmentRating ar ON ar.apartment.id = a.id WHERE ar.user.userCode = :userCode")
    Page<ApartmentRatingVO> findAllApartmentRatingByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.HotelRatingVO(h.name, hr.rating) FROM Hotel h LEFT JOIN HotelRating hr ON hr.hotel.id = h.id WHERE hr.user.userCode = :userCode")
    Page<HotelRatingVO> findAllHotelRatingByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.OrderApartmentVO(a.name, oa.code, oa.amount, oa.discount, oa.status, oa.bill) FROM Apartment a INNER JOIN OrderApartment oa ON oa.apartment.id = a.id WHERE oa.user.userCode = :userCode")
    Page<OrderApartmentVO> findAllOrderApartmentByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.OrderFlightVO(f.code, o.code, o.amount, o.discount, o.status, o.bill) FROM Flight f INNER JOIN OrderFlight o ON o.flight.id = f.id WHERE o.user.userCode = :userCode")
    Page<OrderFlightVO> findAllOrderFlightByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.OrderHotelVO(h.name, oh.code, oh.amount, oh.discount, oh.status, oh.bill) FROM Hotel h INNER JOIN OrderHotel oh ON oh.room.hotel.id = h.id WHERE oh.user.userCode = :userCode")
    Page<OrderHotelVO> findAllOrderHotelByUserId(@Param("userCode") String userCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.RoomAdminVO(r.room_number, r.capacity, r.type, CASE WHEN EXISTS (SELECT 1 FROM RoomBooking rb WHERE rb.room.id = r.id) THEN FALSE ELSE TRUE END) FROM Hotel h INNER JOIN Room r ON r.hotel.id = h.id WHERE h.code = :hotelCode")
    Page<RoomAdminVO> findAllRoomsByHotelCode(@Param("hotelCode") String hotelCode, Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.AirportAdminVO(a.code, a.name,a.iataCode, a.description, a.terminal, a.gate, a.latitude, a.longitude, a.timezone, new com.example.skystayback.dtos.admin.all.CityVO(c.name, new com.example.skystayback.dtos.admin.all.CountryVO(co.name))) FROM Airport a INNER JOIN City c ON a.city.id = c.id INNER JOIN Country co ON c.country.id = co.id")
    Page<AirportAdminVO> getAllAirports(Pageable pageable);

    @Query("SELECT new com.example.skystayback.dtos.admin.all.CityVO(c.name, new com.example.skystayback.dtos.admin.all.CountryVO(co.name)) FROM City c INNER JOIN Country co ON c.country.id = co.id")
    List<CityVO> getAllCities();
}