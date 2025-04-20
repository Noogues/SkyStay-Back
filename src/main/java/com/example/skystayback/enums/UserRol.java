package com.example.skystayback.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRol implements GrantedAuthority {
    ROLE_ADMIN, ROLE_MODERATOR, ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}

