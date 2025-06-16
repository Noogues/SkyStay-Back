package com.example.skystayback.dtos.profile;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
