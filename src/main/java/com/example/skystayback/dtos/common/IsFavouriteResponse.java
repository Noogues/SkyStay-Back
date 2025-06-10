package com.example.skystayback.dtos.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IsFavouriteResponse {
    private boolean isFavorite;

    public IsFavouriteResponse(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

}