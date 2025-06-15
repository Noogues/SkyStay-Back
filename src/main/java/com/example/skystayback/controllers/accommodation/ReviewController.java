package com.example.skystayback.controllers.accommodation;

import com.example.skystayback.models.User;
import com.example.skystayback.services.accommodation.ReviewService;
import com.example.skystayback.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import com.example.skystayback.dtos.common.PagedResponse;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/{type}/{code}")
    public <T> PagedResponse<T> getReviews(
            @PathVariable String type,
            @PathVariable String code,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10", name = "limit") int size,
            @RequestParam(defaultValue = "newest") String sortBy
    ) {
        Page<T> result = (Page<T>) reviewService.getReviews(code, type, page, size, sortBy);
        PagedResponse<T> response = new PagedResponse<>();
        response.setContent(result.getContent());
        response.setPage(result.getNumber() + 1);
        response.setSize(result.getSize());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        return response;
    }

    // POST /reviews
    @PostMapping("")
    public Object submitReview(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ReviewRequest reviewRequest
    ) {
        User user = jwtService.getUserFromToken(authHeader);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o ausente");
        }
        return reviewService.submitReview(
                reviewRequest.getAccommodationType(),
                reviewRequest.getAccommodationCode(),
                reviewRequest.getRating(),
                reviewRequest.getTitle(),
                reviewRequest.getComment(),
                reviewRequest.getPros(),
                reviewRequest.getCons(),
                user
        );
    }

    @PostMapping("/helpful")
    public void markHelpful(@RequestBody MarkHelpfulRequest request) {
        reviewService.markHelpful(request.getReviewId(), request.getAccommodationType());
    }

    public static class MarkHelpfulRequest {
        private Long reviewId;
        private String accommodationType;

        public Long getReviewId() { return reviewId; }
        public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
        public String getAccommodationType() { return accommodationType; }
        public void setAccommodationType(String accommodationType) { this.accommodationType = accommodationType; }
    }

    // DTO para el body del POST /reviews
    public static class ReviewRequest {
        private String accommodationCode;
        private String accommodationType;
        private Float rating;
        private String title;
        private String comment;
        private String pros;
        private String cons;
        // Si usas detailedRatings, agrégalo aquí

        public String getAccommodationCode() { return accommodationCode; }
        public void setAccommodationCode(String accommodationCode) { this.accommodationCode = accommodationCode; }
        public String getAccommodationType() { return accommodationType; }
        public void setAccommodationType(String accommodationType) { this.accommodationType = accommodationType; }
        public Float getRating() { return rating; }
        public void setRating(Float rating) { this.rating = rating; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public String getPros() { return pros; }
        public void setPros(String pros) { this.pros = pros; }
        public String getCons() { return cons; }
        public void setCons(String cons) { this.cons = cons; }
    }
}