package com.example.skystayback.services.email;

import lombok.Getter;

@Getter
public enum EmailTemplateType {
    REGISTRATION("registration"),
    RESET_PASSWORD("resetPassword"),
    BOOKING_CONFIRMATION("booking_confirmation"),
    PASSWORD_RESET("password_reset");

    private final String templateName;

    EmailTemplateType(String templateName) {
        this.templateName = templateName;
    }

}
