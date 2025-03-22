package com.example.skystayback.enums;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum AirplaneConfiguration {

    CONFIG_3_3(new int[]{3, 3}),
    CONFIG_2_3_2(new int[]{2, 3, 2}),
    CONFIG_2_4_2(new int[]{2, 4, 2}),
    CONFIG_3_4_3(new int[]{3, 4, 3}),
    CONFIG_3_3_3(new int[]{3, 3, 3}),
    CONFIG_4_4_4(new int[]{4, 4, 4});

    private final int[] seatingArrangement;

    AirplaneConfiguration(int[] seatingArrangement) {
        this.seatingArrangement = seatingArrangement;
    }

    // Para usar este enumeredo es neceserio ponerle esto al final para que a la hora de mostrarlo aparezca en el formato deseado es decir
    // en formato de lista.
    @JsonValue
    public int[] toArray() {
        return seatingArrangement;
    }
}
