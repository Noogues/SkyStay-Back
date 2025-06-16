package com.example.skystayback.dtos.profile;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileStatsDto {
    private Long totalBookings;
    private Long totalFlights;
    private Long totalReviews;
    private Long totalFavorites;
    private String memberSince;
}
