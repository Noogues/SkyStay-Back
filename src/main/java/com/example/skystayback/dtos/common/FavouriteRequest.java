package com.example.skystayback.dtos.common;

public class FavouriteRequest {
    private String accommodationId;
    private String type;

    public String getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(String accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}