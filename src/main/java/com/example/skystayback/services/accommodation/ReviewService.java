package com.example.skystayback.services.accommodation;

import com.example.skystayback.models.HotelRating;
import com.example.skystayback.models.ApartmentRating;
import com.example.skystayback.models.Hotel;
import com.example.skystayback.models.Apartment;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.HotelRatingRepository;
import com.example.skystayback.repositories.ApartmentRatingRepository;
import com.example.skystayback.repositories.HotelRepository;
import com.example.skystayback.repositories.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private HotelRatingRepository hotelRatingRepository;

    @Autowired
    private ApartmentRatingRepository apartmentRatingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    public Page<?> getReviews(String code, String type, int page, int size, String sortBy) {
        Sort sort = switch (sortBy) {
            case "oldest" -> Sort.by("createdAt").ascending();
            case "rating_high" -> Sort.by("rating").descending();
            case "rating_low" -> Sort.by("rating").ascending();
            default -> Sort.by("createdAt").descending();
        };
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        if ("hotel".equalsIgnoreCase(type)) {
            return hotelRatingRepository.findByHotel_Code(code, pageable);
        } else if ("apartment".equalsIgnoreCase(type)) {
            return apartmentRatingRepository.findByApartment_Code(code, pageable);
        } else {
            throw new IllegalArgumentException("Tipo de alojamiento no válido");
        }
    }

    public Object submitReview(
            String type,
            String code,
            Float rating,
            String title,
            String comment,
            String pros,
            String cons,
            User user
    ) {
        if ("hotel".equalsIgnoreCase(type)) {
            Hotel hotel = hotelRepository.findByCode(code)
                    .orElseThrow(() -> new IllegalArgumentException("Hotel no encontrado"));
            HotelRating hotelReview = new HotelRating();
            hotelReview.setHotel(hotel);
            hotelReview.setUser(user);
            hotelReview.setRating(rating);
            hotelReview.setTitle(title);
            hotelReview.setComment(comment);
            hotelReview.setPros(pros);
            hotelReview.setCons(cons);
            hotelReview.setHelpfulCount(0);
            hotelReview.setCreatedAt(java.time.LocalDateTime.now());
            return hotelRatingRepository.save(hotelReview);
        } else if ("apartment".equalsIgnoreCase(type)) {
            Apartment apartment = apartmentRepository.findByCode(code)
                    .orElseThrow(() -> new IllegalArgumentException("Apartamento no encontrado"));
            ApartmentRating apartmentReview = new ApartmentRating();
            apartmentReview.setApartment(apartment);
            apartmentReview.setUser(user);
            apartmentReview.setRating(rating);
            apartmentReview.setTitle(title);
            apartmentReview.setComment(comment);
            apartmentReview.setPros(pros);
            apartmentReview.setCons(cons);
            apartmentReview.setHelpfulCount(0);
            apartmentReview.setCreatedAt(java.time.LocalDateTime.now());
            return apartmentRatingRepository.save(apartmentReview);
        } else {
            throw new IllegalArgumentException("Tipo de alojamiento no válido");
        }
    }

    public void markHelpful(Long reviewId, String type) {
        if ("hotel".equalsIgnoreCase(type)) {
            HotelRating review = hotelRatingRepository.findById(reviewId).orElseThrow();
            review.setHelpfulCount(review.getHelpfulCount() + 1);
            hotelRatingRepository.save(review);
        } else if ("apartment".equalsIgnoreCase(type)) {
            ApartmentRating review = apartmentRatingRepository.findById(reviewId).orElseThrow();
            review.setHelpfulCount(review.getHelpfulCount() + 1);
            apartmentRatingRepository.save(review);
        } else {
            throw new IllegalArgumentException("Tipo de alojamiento no válido");
        }
    }
}