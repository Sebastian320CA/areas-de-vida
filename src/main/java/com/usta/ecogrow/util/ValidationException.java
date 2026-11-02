package com.usta.ecogrow.util;

/** Error de validación o autenticación con un mensaje listo para el usuario. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
