package com.example.skystayback.controllers.accommodation;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.models.User;
import com.example.skystayback.security.JwtService;
import com.example.skystayback.services.accommodation.FavouriteService;
import com.example.skystayback.services.accommodation.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/accommodations")
public class Global {

    @Autowired
    private GlobalService globalService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("")
    public ResponseVO<List<AccommodationResponseVO>> getAccommodations(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDate checkIn,
            @RequestParam(required = false) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "1") Integer adults,
            @RequestParam(required = false, defaultValue = "0") Integer children,
            @RequestParam(required = false, defaultValue = "1") Integer rooms,
            @RequestParam(required = false) String type) {

        return globalService.searchAccommodations(destination, checkIn, checkOut, adults, children, rooms, type);
    }

    @GetMapping("/cities")
    public List<String> getCities() {
        return globalService.getAllCities();
    }

    @GetMapping("/destinations")
    public List<DestinationVO> getDestinations() {
        return globalService.getDestinations();
    }

    @GetMapping("/destinations/most-rated")
    public List<DestinationVO> getMostRatedDestinations() {
        return globalService.getTopRatedDestinations();
    }

    @GetMapping("/{code}")
    public ResponseVO<AccommodationDetailVO> getAccommodationDetail(
            @PathVariable String code,
            @RequestParam (required = false) String typeAccomodation,
            @RequestParam(required = false) LocalDate checkIn,
            @RequestParam(required = false) LocalDate checkOut,
            @RequestParam(required = false) Integer adults,
            @RequestParam(required = false) Integer children,
            @RequestParam(required = false) Integer rooms) {

        return globalService.getAccommodationDetail(code,typeAccomodation, checkIn, checkOut, adults, children, rooms);
    }


    @PostMapping("/favorites/add")
    public void addFavourite(@RequestHeader("Authorization") String authHeader, @RequestBody FavouriteRequest request) {
        User user = jwtService.getUserFromToken(authHeader);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o ausente");
        }
        favouriteService.addFavourite(request.getAccommodationId(), request.getType(), user);
    }

    @PostMapping("/favorites/remove")
    public void removeFavourite(@RequestHeader("Authorization") String authHeader, @RequestBody FavouriteRequest request) {
        User user = jwtService.getUserFromToken(authHeader);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o ausente");
        }
        favouriteService.removeFavourite(request.getAccommodationId(), request.getType(), user);
    }

    @GetMapping("/favorites/check")
    public IsFavouriteResponse checkFavourite(@RequestHeader("Authorization") String authHeader, @RequestParam String code, @RequestParam String type) {
        User user = jwtService.getUserFromToken(authHeader);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o ausente");
        }
        boolean isFav = favouriteService.isFavourite(code, type, user);
        return new IsFavouriteResponse(isFav);
    }

    @GetMapping("/availability")
    public ResponseVO<List<RoomAvailabilityVO>> checkRoomAvailability(
            @RequestParam String code,
            @RequestParam String type,
            @RequestParam List<String> roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        // Ignoramos las fechas proporcionadas y pasamos null para obtener toda la disponibilidad
        return globalService.checkRoomAvailability(code, type, roomId, null, null);
    }
}