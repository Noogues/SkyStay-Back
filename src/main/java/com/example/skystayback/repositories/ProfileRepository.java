package com.example.skystayback.repositories;


import com.example.skystayback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<User, Long> {

    // ========================
    // USUARIO
    // ========================

    Optional<User> findByUserCode(String userCode);

    // ========================
    // ESTADÍSTICAS - RESERVAS
    // ========================

    @Query("SELECT COUNT(rb) FROM RoomBooking rb WHERE rb.user.id = :userId")
    Long countHotelBookingsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(rba) FROM RoomAparmentBooking rba WHERE rba.user.id = :userId")
    Long countApartmentBookingsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(sb) FROM SeatBooking sb WHERE sb.user.id = :userId")
    Long countFlightBookingsByUserId(@Param("userId") Long userId);

    // ========================
    // ESTADÍSTICAS - RESEÑAS
    // ========================

    @Query("SELECT COUNT(hr) FROM HotelRating hr WHERE hr.user.id = :userId")
    Long countHotelReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(ar) FROM ApartmentRating ar WHERE ar.user.id = :userId")
    Long countApartmentReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(alr) FROM AirlineRating alr WHERE alr.user.id = :userId")
    Long countAirlineReviewsByUserId(@Param("userId") Long userId);

    // ========================
    // ESTADÍSTICAS - FAVORITOS
    // ========================

    @Query("SELECT COUNT(fh) FROM FavouriteHotel fh WHERE fh.user.id = :userId")
    Long countHotelFavoritesByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(fa) FROM FavouriteApartment fa WHERE fa.user.id = :userId")
    Long countApartmentFavoritesByUserId(@Param("userId") Long userId);

    // ========================
    // RESERVAS - HOTELES
    // ========================

    @Query(value = """
        SELECT rb.id, rb.start_date, rb.end_date, rb.status,
               h.name, h.code, c.name as city_name,
               oh.amount, r.room_number,
               rch.price, rc.room_type
        FROM room_booking rb
        JOIN room r ON rb.room_id = r.id
        JOIN room_configuration_hotel rch ON r.room_configuration_hotel_id = rch.id
        JOIN room_configuration rc ON rch.room_configuration_id = rc.id
        JOIN hotel h ON rch.hotel_id = h.id
        JOIN city c ON h.city_id = c.id
        LEFT JOIN order_hotel oh ON oh.room_booking_id = rb.id
        WHERE rb.user_id = :userId
        ORDER BY rb.start_date DESC
        LIMIT :size OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> getHotelBookingsByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    // ========================
    // RESERVAS - APARTAMENTOS
    // ========================

    @Query(value = """
        SELECT rba.id, rba.start_date, rba.end_date, rba.status,
               a.name, a.code, c.name as city_name,
               oa.amount, ra.room_number,
               rca.price, rc.room_type
        FROM room_booking_apartment rba
        JOIN room_apartment ra ON rba.room_apartment_id = ra.id
        JOIN room_configuration_apartment rca ON ra.room_configuration_apartment_id = rca.id
        JOIN room_configuration rc ON rca.room_configuration_id = rc.id
        JOIN apartment a ON rca.apartment_id = a.id
        JOIN city c ON a.city_id = c.id
        LEFT JOIN order_apartment oa ON oa.room_apartment_booking_id = rba.id
        WHERE rba.user_id = :userId
        ORDER BY rba.start_date DESC
        LIMIT :size OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> getApartmentBookingsByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    // ========================
    // RESERVAS - VUELOS
    // ========================

    @Query(value = """
    SELECT sb.id, f.code, f.date_time, f.date_time_arrival,
           al.name as airline_name, al.code as airline_code,
           dep_airport.name as departure_airport, dep_city.name as departure_city,
           arr_airport.name as arrival_airport, arr_city.name as arrival_city,
           fss.price, s.seat_row, s.seat_column,
           flight_order.amount, sb.name, sb.surnames
    FROM seatbooking sb
    JOIN flight_seat_status fss ON sb.flight_seat_status_id = fss.id
    JOIN flight f ON fss.flight_id = f.id
    JOIN seat s ON fss.seat_id = s.id
    JOIN airline al ON f.airline_id = al.id
    JOIN airport dep_airport ON f.departure_airport_id = dep_airport.id
    JOIN airport arr_airport ON f.arrival_airport_id = arr_airport.id
    JOIN city dep_city ON dep_airport.city_id = dep_city.id
    JOIN city arr_city ON arr_airport.city_id = arr_city.id
    JOIN order_flight flight_order ON sb.order_flight_id = flight_order.id
    WHERE sb.user_id = :userId
    ORDER BY f.date_time DESC
    LIMIT :size OFFSET :offset
    """, nativeQuery = true)
    List<Object[]> getFlightBookingsByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);
    // ========================
    // CONTADORES TOTALES
    // ========================

    @Query(value = """
        SELECT (
            (SELECT COUNT(*) FROM room_booking WHERE user_id = :userId) +
            (SELECT COUNT(*) FROM room_booking_apartment WHERE user_id = :userId)
        )
        """, nativeQuery = true)
    Long countTotalBookingsByUserId(@Param("userId") Long userId);

    @Query(value = """
        SELECT (
            (SELECT COUNT(*) FROM hotel_rating WHERE user_id = :userId) +
            (SELECT COUNT(*) FROM apartment_rating WHERE user_id = :userId) +
            (SELECT COUNT(*) FROM airline_rating WHERE user_id = :userId)
        )
        """, nativeQuery = true)
    Long countTotalReviewsByUserId(@Param("userId") Long userId);

    // ========================
    // RESEÑAS
    // ========================

    @Query(value = """
        SELECT hr.id, hr.rating, hr.title, hr.comment, hr.created_at,
               h.name, h.code, c.name as city_name
        FROM hotel_rating hr
        JOIN hotel h ON hr.hotel_id = h.id
        JOIN city c ON h.city_id = c.id
        WHERE hr.user_id = :userId
        ORDER BY hr.created_at DESC
        LIMIT :size OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> getHotelReviewsByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    @Query(value = """
        SELECT ar.id, ar.rating, ar.title, ar.comment, ar.created_at,
               a.name, a.code, c.name as city_name
        FROM apartment_rating ar
        JOIN apartment a ON ar.apartment_id = a.id
        JOIN city c ON a.city_id = c.id
        WHERE ar.user_id = :userId
        ORDER BY ar.created_at DESC
        LIMIT :size OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> getApartmentReviewsByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    @Query(value = """
        SELECT alr.id, alr.rating, al.name, al.code
        FROM airline_rating alr
        JOIN airline al ON alr.airline_id = al.id
        WHERE alr.user_id = :userId
        ORDER BY alr.id DESC
        LIMIT :size OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> getAirlineReviewsByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    // ========================
    // FAVORITOS
    // ========================

    @Query(value = """
SELECT h.code, h.name, h.stars, h.description,
       c.name as city_name, MIN(rch.price) as price
FROM favourite_hotel fh
JOIN hotel h ON fh.hotel_id = h.id
JOIN city c ON h.city_id = c.id
LEFT JOIN room_configuration_hotel rch ON rch.hotel_id = h.id
WHERE fh.user_id = :userId
GROUP BY h.id, h.code, h.name, h.stars, h.description, c.name
ORDER BY MAX(fh.id) DESC
""", nativeQuery = true)
    List<Object[]> getHotelFavoritesByUserId(@Param("userId") Long userId);

    @Query(value = """
SELECT a.code, a.name, a.stars, a.description,
       c.name as city_name, MIN(rca.price) as price
FROM favourite_apartment fa
JOIN apartment a ON fa.apartment_id = a.id
JOIN city c ON a.city_id = c.id
LEFT JOIN room_configuration_apartment rca ON rca.apartment_id = a.id
WHERE fa.user_id = :userId
GROUP BY a.id, a.code, a.name, a.stars, a.description, c.name
ORDER BY MAX(fa.id) DESC
""", nativeQuery = true)
    List<Object[]> getApartmentFavoritesByUserId(@Param("userId") Long userId);

    // ========================
    // ELIMINAR FAVORITOS
    // ========================

    @Modifying
    @Transactional
    @Query(value = """
        DELETE fh FROM favourite_hotel fh
        JOIN hotel h ON fh.hotel_id = h.id
        WHERE fh.user_id = :userId AND h.code = :accommodationCode
        """, nativeQuery = true)
    void removeHotelFavorite(@Param("userId") Long userId, @Param("accommodationCode") String accommodationCode);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE fa FROM favourite_apartment fa
        JOIN apartment a ON fa.apartment_id = a.id
        WHERE fa.user_id = :userId AND a.code = :accommodationCode
        """, nativeQuery = true)
    void removeApartmentFavorite(@Param("userId") Long userId, @Param("accommodationCode") String accommodationCode);
}