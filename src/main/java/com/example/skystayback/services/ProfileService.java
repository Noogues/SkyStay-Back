package com.example.skystayback.services;


import com.example.skystayback.dtos.profile.*;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========================
    // PERFIL DE USUARIO
    // ========================

    public UserProfileDto getUserProfile(String userCode) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return UserProfileDto.builder()
                .userCode(user.getUserCode())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .img(user.getImg())
                .validation(user.getValidation())
                .rol(user.getRol().toString())
                .birthDate(user.getBirthDate().toString())
                .gender(user.getGender() != null ? user.getGender().toString() : null)
                .nif(user.getNif())
                .createdAt(LocalDateTime.now().toString()) // Asumiendo que tienes un campo createdAt
                .build();
    }

    public UserProfileDto updateUserProfile(String userCode, UpdateProfileDto updateProfileDto) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(updateProfileDto.getName());
        user.setLastName(updateProfileDto.getLastName());
        user.setEmail(updateProfileDto.getEmail());
        user.setPhone(updateProfileDto.getPhone());

        if (updateProfileDto.getBirthDate() != null) {
            LocalDate localDate = LocalDate.parse(updateProfileDto.getBirthDate());
            java.util.Date date = java.util.Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            user.setBirthDate(date);
        }

        if (updateProfileDto.getGender() != null) {
            user.setGender(Integer.valueOf(updateProfileDto.getGender()));
        }

        if (updateProfileDto.getNif() != null) {
            user.setNif(updateProfileDto.getNif());
        }

        User savedUser = profileRepository.save(user);
        return getUserProfile(savedUser.getUserCode());
    }

    public String uploadProfileImage(String userCode, MultipartFile file) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aquí implementarías la lógica para subir la imagen
        // Por ejemplo, a Cloudinary o AWS S3
        String imageUrl = uploadImageToCloud(file);

        user.setImg(imageUrl);
        profileRepository.save(user);

        return imageUrl;
    }

    // ========================
    // ESTADÍSTICAS
    // ========================

    public ProfileStatsDto getProfileStats(String userCode) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long userId = user.getId();

        // Contar reservas de hoteles
        Long hotelBookings = profileRepository.countHotelBookingsByUserId(userId);

        // Contar reservas de apartamentos
        Long apartmentBookings = profileRepository.countApartmentBookingsByUserId(userId);

        // Contar reservas de vuelos
        Long flightBookings = profileRepository.countFlightBookingsByUserId(userId);

        // Contar reseñas
        Long hotelReviews = profileRepository.countHotelReviewsByUserId(userId);
        Long apartmentReviews = profileRepository.countApartmentReviewsByUserId(userId);
        Long airlineReviews = profileRepository.countAirlineReviewsByUserId(userId);

        // Contar favoritos
        Long hotelFavorites = profileRepository.countHotelFavoritesByUserId(userId);
        Long apartmentFavorites = profileRepository.countApartmentFavoritesByUserId(userId);

        return ProfileStatsDto.builder()
                .totalBookings(hotelBookings + apartmentBookings)
                .totalFlights(flightBookings)
                .totalReviews(hotelReviews + apartmentReviews + airlineReviews)
                .totalFavorites(hotelFavorites + apartmentFavorites)
                .memberSince(user.getBirthDate().toString()) // O la fecha de creación del usuario
                .build();
    }

    // ========================
    // RESERVAS
    // ========================

    public BookingsResponseDto getUserBookings(String userCode, int page, int size) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener reservas de hoteles
        List<Object[]> hotelBookings = profileRepository.getHotelBookingsByUserId(user.getId(), page, size);

        // Obtener reservas de apartamentos
        List<Object[]> apartmentBookings = profileRepository.getApartmentBookingsByUserId(user.getId(), page, size);

        // Convertir a DTOs
        List<BookingDto> bookings = hotelBookings.stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());

        bookings.addAll(apartmentBookings.stream()
                .map(this::mapToApartmentBookingDto)
                .toList());

        Long totalCount = profileRepository.countTotalBookingsByUserId(user.getId());

        return BookingsResponseDto.builder()
                .items(bookings)
                .totalItems(totalCount.intValue())
                .totalPages((int) Math.ceil((double) totalCount / size))
                .currentPage(page)
                .build();
    }

    public FlightsResponseDto getUserFlights(String userCode, int page, int size) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Object[]> flightBookings = profileRepository.getFlightBookingsByUserId(user.getId(), page, size);

        List<FlightDto> flights = flightBookings.stream()
                .map(this::mapToFlightDto)
                .collect(Collectors.toList());

        Long totalCount = profileRepository.countFlightBookingsByUserId(user.getId());

        return FlightsResponseDto.builder()
                .items(flights)
                .totalItems(totalCount.intValue())
                .totalPages((int) Math.ceil((double) totalCount / size))
                .currentPage(page)
                .build();
    }

    // ========================
    // RESEÑAS
    // ========================

    public ReviewsResponseDto getUserReviews(String userCode, int page, int size) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener reseñas de hoteles
        List<Object[]> hotelReviews = profileRepository.getHotelReviewsByUserId(user.getId(), page, size);

        // Obtener reseñas de apartamentos
        List<Object[]> apartmentReviews = profileRepository.getApartmentReviewsByUserId(user.getId(), page, size);

        // Obtener reseñas de aerolíneas
        List<Object[]> airlineReviews = profileRepository.getAirlineReviewsByUserId(user.getId(), page, size);

        List<ReviewDto> reviews = hotelReviews.stream()
                .map(this::mapToHotelReviewDto)
                .collect(Collectors.toList());

        reviews.addAll(apartmentReviews.stream()
                .map(this::mapToApartmentReviewDto)
                .toList());

        reviews.addAll(airlineReviews.stream()
                .map(this::mapToAirlineReviewDto)
                .toList());

        Long totalCount = profileRepository.countTotalReviewsByUserId(user.getId());

        return ReviewsResponseDto.builder()
                .items(reviews)
                .totalItems(totalCount.intValue())
                .totalPages((int) Math.ceil((double) totalCount / size))
                .currentPage(page)
                .build();
    }

    public ReviewDto createReview(String userCode, CreateReviewDto createReviewDto) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Implementar según el tipo de reseña
        if ("accommodation".equals(createReviewDto.getType())) {
            return createAccommodationReview(user, createReviewDto);
        } else if ("airline".equals(createReviewDto.getType())) {
            return createAirlineReview(user, createReviewDto);
        }

        throw new RuntimeException("Tipo de reseña no válido");
    }

    public ReviewDto updateReview(String userCode, Long reviewId, UpdateReviewDto updateReviewDto) {
        // Implementar actualización de reseña
        return null;
    }

    public void deleteReview(String userCode, Long reviewId) {
        // Implementar eliminación de reseña
    }

    // ========================
    // FAVORITOS
    // ========================

    public FavoritesDto getFavorites(String userCode) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener favoritos de hoteles
        List<Object[]> hotelFavorites = profileRepository.getHotelFavoritesByUserId(user.getId());

        // Obtener favoritos de apartamentos
        List<Object[]> apartmentFavorites = profileRepository.getApartmentFavoritesByUserId(user.getId());

        List<FavoriteDto> favorites = hotelFavorites.stream()
                .map(this::mapToHotelFavoriteDto)
                .collect(Collectors.toList());

        favorites.addAll(apartmentFavorites.stream()
                .map(this::mapToApartmentFavoriteDto)
                .toList());

        return FavoritesDto.builder()
                .favorites(favorites)
                .build();
    }

    public void removeFavorite(String userCode, String accommodationCode, String type) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if ("hotel".equalsIgnoreCase(type)) {
            profileRepository.removeHotelFavorite(user.getId(), accommodationCode);
        } else if ("apartment".equalsIgnoreCase(type)) {
            profileRepository.removeApartmentFavorite(user.getId(), accommodationCode);
        }
    }

    // ========================
    // SEGURIDAD
    // ========================

    public void changePassword(String userCode, ChangePasswordDto changePasswordDto) {
        User user = profileRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        // Encriptar nueva contraseña
        String encodedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
        user.setPassword(encodedPassword);

        profileRepository.save(user);
    }

    // ========================
    // MÉTODOS AUXILIARES
    // ========================

    private String uploadImageToCloud(MultipartFile file) {
        // Implementar subida a Cloudinary/AWS S3
        return "https://ejemplo.com/imagen.jpg";
    }

    private BookingDto mapToBookingDto(Object[] row) {
        return BookingDto.builder()
                .id(((Number) row[0]).longValue())
                .startDate(row[1].toString())
                .endDate(row[2].toString())
                .status(row[3].toString())
                .accommodationName((String) row[4])
                .code((String) row[5])
                .cityName((String) row[6])
                .totalAmount(((Number) row[7]).doubleValue())
                .roomNumber(((Number) row[8]).intValue())
                .roomPrice(((Number) row[9]).doubleValue())
                .roomType(getRoomTypeName(((Number) row[10]).intValue()))
                .type("hotel")
                .isPast(isDateInPast(row[2].toString()))
                .isCurrent(isDateCurrent(row[1].toString(), row[2].toString()))
                .isFuture(isDateInFuture(row[1].toString()))
                .canReview(isDateInPast(row[2].toString()))
                .build();
    }

    private BookingDto mapToApartmentBookingDto(Object[] row) {
        return BookingDto.builder()
                .id(((Number) row[0]).longValue())
                .startDate(row[1].toString())
                .endDate(row[2].toString())
                .status(row[3].toString())
                .accommodationName((String) row[4])
                .code((String) row[5])
                .cityName((String) row[6])
                .totalAmount(((Number) row[7]).doubleValue())
                .roomNumber(((Number) row[8]).intValue())
                .roomPrice(((Number) row[9]).doubleValue())
                .roomType(getRoomTypeName(((Number) row[10]).intValue()))
                .type("apartment")
                .isPast(isDateInPast(row[2].toString()))
                .isCurrent(isDateCurrent(row[1].toString(), row[2].toString()))
                .isFuture(isDateInFuture(row[1].toString()))
                .canReview(isDateInPast(row[2].toString()))
                .build();
    }

    private FlightDto mapToFlightDto(Object[] row) {
        return FlightDto.builder()
                .id(((Number) row[0]).longValue())
                .flightCode((String) row[1])
                .departureDateTime(row[2].toString())
                .arrivalDateTime(row[3].toString())
                .airlineName((String) row[4])
                .airlineCode((String) row[5])
                .departureAirportName((String) row[6])
                .departureCityName((String) row[7])
                .arrivalAirportName((String) row[8])
                .arrivalCityName((String) row[9])
                .seatPrice(((Number) row[10]).doubleValue())
                .seatRow((String) row[11])
                .seatColumn((String) row[12])
                .totalAmount(((Number) row[13]).doubleValue())
                .passengerName((String) row[14])
                .passengerSurnames((String) row[15])
                .seatNumber(String.valueOf(row[11]) + String.valueOf(row[12]))
                .isPast(isDateInPast(row[2].toString()))
                .isCurrent(isDateCurrent(row[2].toString(), row[3].toString()))
                .isFuture(isDateInFuture(row[2].toString()))
                .canReview(isDateInPast(row[3].toString()))
                .build();
    }

    private ReviewDto mapToHotelReviewDto(Object[] row) {
        return ReviewDto.builder()
                .id(((Number) row[0]).longValue())
                .rating(((Number) row[1]).floatValue())
                .title((String) row[2])
                .comment((String) row[3])
                .createdAt(row[4].toString())
                .entityName((String) row[5])
                .entityCode((String) row[6])
                .cityName((String) row[7])
                .type("hotel")
                .canEdit(true)
                .canDelete(true)
                .build();
    }

    private ReviewDto mapToApartmentReviewDto(Object[] row) {
        return ReviewDto.builder()
                .id(((Number) row[0]).longValue())
                .rating(((Number) row[1]).floatValue())
                .title((String) row[2])
                .comment((String) row[3])
                .createdAt(row[4].toString())
                .entityName((String) row[5])
                .entityCode((String) row[6])
                .cityName((String) row[7])
                .type("apartment")
                .canEdit(true)
                .canDelete(true)
                .build();
    }

    private ReviewDto mapToAirlineReviewDto(Object[] row) {
        return ReviewDto.builder()
                .id(((Number) row[0]).longValue())
                .rating(((Number) row[1]).floatValue())
                .entityName((String) row[2])
                .entityCode((String) row[3])
                .type("airline")
                .canEdit(true)
                .canDelete(true)
                .build();
    }

    private ReviewDto createAccommodationReview(User user, CreateReviewDto createReviewDto) {
        // Primero intentar buscar un hotel con ese código
        Long hotelId = profileRepository.findHotelIdByCode(createReviewDto.getEntityCode());
        if (hotelId != null) {
            // Es un hotel
            // Verificar si el usuario ha reservado este hotel
            boolean hasBooking = profileRepository.hasUserBookedHotel(user.getId(), hotelId);
            if (!hasBooking) {
                throw new RuntimeException("No puedes reseñar un hotel que no has reservado");
            }

            // Verificar si ya existe una reseña
            boolean hasReview = profileRepository.hasUserReviewedHotel(user.getId(), hotelId);
            if (hasReview) {
                throw new RuntimeException("Ya has reseñado este hotel");
            }

            // Crear reseña
            Long reviewId = profileRepository.createHotelReview(
                    user.getId(),
                    hotelId,
                    createReviewDto.getRating(),
                    createReviewDto.getTitle(),
                    createReviewDto.getComment()
            );

            // Recuperar la reseña creada
            Object[] reviewData = profileRepository.getHotelReviewById(reviewId);
            return mapToHotelReviewDto(reviewData);
        } else {
            // Intentar buscar un apartamento con ese código
            Long apartmentId = profileRepository.findApartmentIdByCode(createReviewDto.getEntityCode());
            if (apartmentId != null) {
                // Es un apartamento
                // Verificar si el usuario ha reservado este apartamento
                boolean hasBooking = profileRepository.hasUserBookedApartment(user.getId(), apartmentId);
                if (!hasBooking) {
                    throw new RuntimeException("No puedes reseñar un apartamento que no has reservado");
                }

                // Verificar si ya existe una reseña
                boolean hasReview = profileRepository.hasUserReviewedApartment(user.getId(), apartmentId);
                if (hasReview) {
                    throw new RuntimeException("Ya has reseñado este apartamento");
                }

                // Crear reseña
                Long reviewId = profileRepository.createApartmentReview(
                        user.getId(),
                        apartmentId,
                        createReviewDto.getRating(),
                        createReviewDto.getTitle(),
                        createReviewDto.getComment()
                );

                // Recuperar la reseña creada
                Object[] reviewData = profileRepository.getApartmentReviewById(reviewId);
                return mapToApartmentReviewDto(reviewData);
            } else {
                throw new RuntimeException("Alojamiento no encontrado");
            }
        }
    }

    private ReviewDto createAirlineReview(User user, CreateReviewDto createReviewDto) {
        // Buscar aerolínea por código
        Long airlineId = profileRepository.findAirlineIdByCode(createReviewDto.getEntityCode());
        if (airlineId == null) {
            throw new RuntimeException("Aerolínea no encontrada");
        }

        // Verificar si el usuario ha volado con esta aerolínea
        boolean hasFlown = profileRepository.hasUserFlownWithAirline(user.getId(), airlineId);
        if (!hasFlown) {
            throw new RuntimeException("No puedes reseñar una aerolínea con la que no has volado");
        }

        // Verificar si ya existe una reseña
        boolean hasReview = profileRepository.hasUserReviewedAirline(user.getId(), airlineId);
        if (hasReview) {
            throw new RuntimeException("Ya has reseñado esta aerolínea");
        }

        // Crear reseña (las aerolíneas solo tienen rating, sin título ni comentario)
        Long reviewId = profileRepository.createAirlineReview(
                user.getId(),
                airlineId,
                createReviewDto.getRating()
        );

        // Recuperar la reseña creada
        Object[] reviewData = profileRepository.getAirlineReviewById(reviewId);
        return mapToAirlineReviewDto(reviewData);
    }

    private FavoriteDto mapToHotelFavoriteDto(Object[] row) {
        return FavoriteDto.builder()
                .code((String) row[0])
                .name((String) row[1])
                .stars(((Number) row[2]).intValue())
                .mainImage((String) row[6])
                .description((String) row[3])
                .cityName((String) row[4])
                .minPrice(row[5] != null ? ((Number) row[5]).doubleValue() : null)
                .type("hotel")
                .build();
    }

    private FavoriteDto mapToApartmentFavoriteDto(Object[] row) {
        return FavoriteDto.builder()
                .code((String) row[0])
                .name((String) row[1])
                .stars(((Number) row[2]).intValue())
                .mainImage((String) row[6])
                .description((String) row[3])
                .cityName((String) row[4])
                .minPrice(row[5] != null ? ((Number) row[5]).doubleValue() : null)
                .type("apartment")
                .build();
    }

    private boolean isDateInPast(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            return date.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDateCurrent(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            LocalDate now = LocalDate.now();
            return (now.isEqual(start) || now.isAfter(start)) && (now.isEqual(end) || now.isBefore(end));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDateInFuture(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            return date.isAfter(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private String getRoomTypeName(int roomType) {
        return switch (roomType) {
            case 0 -> "Individual";
            case 1 -> "Doble";
            case 2 -> "Triple";
            case 3 -> "Cuádruple";
            case 4 -> "Suite";
            case 5 -> "Familiar";
            default -> "Desconocido";
        };
    }
}
