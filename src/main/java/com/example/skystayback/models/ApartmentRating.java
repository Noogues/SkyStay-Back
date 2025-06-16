package com.example.skystayback.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "apartment_rating")
public class ApartmentRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(name = "rating", nullable = false, scale = 2)
    private Float rating;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "pros", columnDefinition = "TEXT")
    private String pros;

    @Column(name = "cons", columnDefinition = "TEXT")
    private String cons;

    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}