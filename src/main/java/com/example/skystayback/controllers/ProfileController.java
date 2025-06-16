package com.example.skystayback.controllers;


import com.example.skystayback.dtos.profile.ApiMessage;
import com.example.skystayback.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.skystayback.dtos.profile.*;
import jakarta.servlet.http.HttpServletRequest;
import com.example.skystayback.security.JwtService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtService jwtService;

    // ========================
    // PERFIL DE USUARIO
    // ========================

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            UserProfileDto profile = profileService.getUserProfile(userCode);

            return ResponseEntity.ok(new ApiResponse<>(
                    profile,
                    new ApiMessage("Perfil obtenido correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al obtener el perfil: " + e.getMessage(), 400)
            ));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateUserProfile(
            @RequestBody UpdateProfileDto updateProfileDto,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            UserProfileDto updatedProfile = profileService.updateUserProfile(userCode, updateProfileDto);

            return ResponseEntity.ok(new ApiResponse<>(
                    updatedProfile,
                    new ApiMessage("Perfil actualizado correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al actualizar el perfil: " + e.getMessage(), 400)
            ));
        }
    }

    @PostMapping("/profile/image")
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(
            @RequestParam("image") MultipartFile file,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            String imageUrl = profileService.uploadProfileImage(userCode, file);

            return ResponseEntity.ok(new ApiResponse<>(
                    imageUrl,
                    new ApiMessage("Imagen actualizada correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al subir la imagen: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // ESTADÍSTICAS
    // ========================

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<ProfileStatsDto>> getProfileStats(HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            ProfileStatsDto stats = profileService.getProfileStats(userCode);

            return ResponseEntity.ok(new ApiResponse<>(
                    stats,
                    new ApiMessage("Estadísticas obtenidas correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al obtener estadísticas: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // RESERVAS DE ALOJAMIENTOS
    // ========================

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingsResponseDto>> getUserBookings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            BookingsResponseDto bookings = profileService.getUserBookings(userCode, page, size);

            return ResponseEntity.ok(new ApiResponse<>(
                    bookings,
                    new ApiMessage("Reservas obtenidas correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al obtener reservas: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // RESERVAS DE VUELOS
    // ========================

    @GetMapping("/flights")
    public ResponseEntity<ApiResponse<FlightsResponseDto>> getUserFlights(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            FlightsResponseDto flights = profileService.getUserFlights(userCode, page, size);

            return ResponseEntity.ok(new ApiResponse<>(
                    flights,
                    new ApiMessage("Vuelos obtenidos correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al obtener vuelos: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // RESEÑAS
    // ========================

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewsResponseDto>> getUserReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            ReviewsResponseDto reviews = profileService.getUserReviews(userCode, page, size);

            return ResponseEntity.ok(new ApiResponse<>(
                    reviews,
                    new ApiMessage("Reseñas obtenidas correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al obtener reseñas: " + e.getMessage(), 400)
            ));
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @RequestBody CreateReviewDto createReviewDto,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            ReviewDto review = profileService.createReview(userCode, createReviewDto);

            return ResponseEntity.ok(new ApiResponse<>(
                    review,
                    new ApiMessage("Reseña creada correctamente", 201)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al crear reseña: " + e.getMessage(), 400)
            ));
        }
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewDto updateReviewDto,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            ReviewDto review = profileService.updateReview(userCode, reviewId, updateReviewDto);

            return ResponseEntity.ok(new ApiResponse<>(
                    review,
                    new ApiMessage("Reseña actualizada correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al actualizar reseña: " + e.getMessage(), 400)
            ));
        }
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            profileService.deleteReview(userCode, reviewId);

            return ResponseEntity.ok(new ApiResponse<>(
                    null,
                    new ApiMessage("Reseña eliminada correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al eliminar reseña: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // FAVORITOS
    // ========================

    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<FavoritesDto>> getFavorites(HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            FavoritesDto favorites = profileService.getFavorites(userCode);

            return ResponseEntity.ok(new ApiResponse<>(
                    favorites,
                    new ApiMessage("Favoritos obtenidos correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al obtener favoritos: " + e.getMessage(), 400)
            ));
        }
    }

    @DeleteMapping("/favorites/{accommodationCode}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @PathVariable String accommodationCode,
            @RequestParam String type,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            profileService.removeFavorite(userCode, accommodationCode, type);

            return ResponseEntity.ok(new ApiResponse<>(
                    null,
                    new ApiMessage("Favorito eliminado correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al eliminar favorito: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // SEGURIDAD
    // ========================

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            HttpServletRequest request) {
        try {
            String userCode = extractUserCodeFromToken(request);
            profileService.changePassword(userCode, changePasswordDto);

            return ResponseEntity.ok(new ApiResponse<>(
                    null,
                    new ApiMessage("Contraseña actualizada correctamente", 200)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    null,
                    new ApiMessage("Error al cambiar contraseña: " + e.getMessage(), 400)
            ));
        }
    }

    // ========================
    // MÉTODOS AUXILIARES
    // ========================

    private String extractUserCodeFromToken(HttpServletRequest request) {
        // Implementar extracción del userCode desde el JWT token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUserCode(token);
        }
        throw new RuntimeException("Token no válido");
    }
}
