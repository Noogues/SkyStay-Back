package com.example.skystayback.dtos.profile;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private T objects;
    private ApiMessage messages;

}
