package com.usta.ecogrow;

/** Punto de entrada para empaquetar en un JAR (evita el error "JavaFX runtime components are missing"). */
public final class Launcher {
    private Launcher() {}

    public static void main(String[] args) {
        App.main(args);
    }
}
