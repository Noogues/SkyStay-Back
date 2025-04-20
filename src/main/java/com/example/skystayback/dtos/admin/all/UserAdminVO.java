package com.example.skystayback.dtos.admin.all;
import com.example.skystayback.enums.UserRol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminVO {

    private String userCode;
    private String name;
    private String lastName;
    private String email;
    private String nif;
    private String phone;
    private UserRol rol;
}
