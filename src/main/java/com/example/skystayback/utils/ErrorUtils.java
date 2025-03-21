package com.example.skystayback.utils;

import com.example.skystayback.exceptions.ApiException;

public class ErrorUtils {

    public static void throwApiError(String title, String message, String errorCode) {
        throw new ApiException(title, message, errorCode);
    }

    // Common error cases for reuse
    public static void throwEmailExistsError() {
        throwApiError("Email duplicado", "El email ya está registrado", "EMAIL_ALREADY_EXISTS");
    }

    public static void throwUserNotFoundError() {
        throwApiError("Usuario no encontrado", "No se ha encontrado el usuario solicitado", "USER_NOT_FOUND");
    }

    public static void throwInvalidCredentialsError() {
        throwApiError("Credenciales inválidas", "Las credenciales proporcionadas son incorrectas", "INVALID_CREDENTIALS");
    }

}