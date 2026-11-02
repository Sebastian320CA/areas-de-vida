package com.usta.ecogrow.db;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/** Error de acceso a datos con un mensaje apto para mostrar al usuario. */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(SQLException cause) {
        super(friendly(cause), cause);
    }

    private static String friendly(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            if (e.getErrorCode() == 1062) {
                return "Ya existe un registro con ese valor único (por ejemplo, el correo o el número de documento).";
            }
            return "La operación viola una relación con otros datos (hay registros que dependen de este).";
        }
        String state = e.getSQLState();
        if (state != null && state.startsWith("08")) {
            return "No hay conexión con la base de datos. Verifica que MySQL esté encendido.";
        }
        return "Error de base de datos: " + e.getMessage();
    }
}
