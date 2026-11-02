package com.usta.ecogrow.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

/** Hash y verificación de contraseñas con BCrypt (costo 12). */
public final class PasswordHasher {

    private static final int COST = 12;
    private static volatile String dummyHash;

    private PasswordHasher() {}

    public static String hash(String password) {
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }

    public static boolean verify(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }

    /** Verifica contra un hash falso para que el tiempo de respuesta no revele si el correo existe. */
    public static void burn(String password) {
        if (dummyHash == null) {
            dummyHash = hash("ecogrow-dummy");
        }
        verify(password, dummyHash);
    }
}
