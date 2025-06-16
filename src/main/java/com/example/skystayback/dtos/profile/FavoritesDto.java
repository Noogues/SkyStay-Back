package com.example.skystayback.dtos.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesDto {
    private List<FavoriteDto> favorites;
    private int totalFavorites;
    private int totalHotels;
    private int totalApartments;
}
