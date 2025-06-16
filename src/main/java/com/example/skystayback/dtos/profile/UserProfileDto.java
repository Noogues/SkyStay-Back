package com.example.skystayback.dtos.profile;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String userCode;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String img;
    private Boolean validation;
    private String rol;
    private String birthDate;
    private String gender;
    private String nif;
    private String createdAt;
}
