package com.usta.ecogrow.util;

import com.usta.ecogrow.db.DataAccessException;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class Errors {
    private Errors() {}

    /** Devuelve el mensaje más útil para mostrar al usuario. */
    public static String message(Throwable t) {
        Throwable cur = t;
        while ((cur instanceof CompletionException || cur instanceof ExecutionException) && cur.getCause() != null) {
            cur = cur.getCause();
        }
        if (cur instanceof ValidationException || cur instanceof DataAccessException) {
            return cur.getMessage();
        }
        Throwable root = cur;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return "Ocurrió un error inesperado: " + (root.getMessage() == null ? root.getClass().getSimpleName() : root.getMessage());
    }
}
