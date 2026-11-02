package com.usta.ecogrow.model;

public record User(int id, String name, String email, String passwordHash, Role role, String document, String phone) {

    public User withId(int newId) {
        return new User(newId, name, email, passwordHash, role, document, phone);
    }

    public User withPasswordHash(String hash) {
        return new User(id, name, email, hash, role, document, phone);
    }

    /** Iniciales para el avatar. */
    public String initials() {
        String[] parts = name.trim().split("\\s+");
        String first = parts[0].isEmpty() ? "?" : parts[0].substring(0, 1);
        String second = parts.length > 1 ? parts[1].substring(0, 1) : "";
        return (first + second).toUpperCase();
    }
}
