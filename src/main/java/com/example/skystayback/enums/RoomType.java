package com.example.skystayback.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomType {
    Single,  // Habitación individual
    Double,  // Habitación doble
    Triple,  // Habitación triple
    Suit     // Habitación suite
}
