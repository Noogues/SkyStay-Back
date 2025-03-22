package com.example.skystayback.dtos;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    private String name;
    private String lastName;
    private String email;
    private Date birthDate;
    private String nif;
    private String phone;
    private Integer gender;
    private String img;
    private String password;

}
