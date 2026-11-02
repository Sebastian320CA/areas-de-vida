package com.usta.ecogrow.model;

public enum Role {
    ADMIN("Administrador"),
    AUTHORITY("Autoridad ambiental"),
    OWNER("Propietario");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    /** Puede crear, editar y eliminar registros del sistema. */
    public boolean canWrite() {
        return this == ADMIN || this == AUTHORITY;
    }

    public boolean canManageUsers() {
        return this == ADMIN;
    }

    @Override
    public String toString() {
        return label;
    }

    public static Role fromDb(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return OWNER;
        }
    }
}
