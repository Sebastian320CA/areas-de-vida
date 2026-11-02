package com.usta.ecogrow.db;

import com.usta.ecogrow.config.AppConfig;
import com.usta.ecogrow.model.Role;
import com.usta.ecogrow.security.PasswordHasher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Prepara la base de datos al arrancar: crea la base y las tablas si no existen,
 * la cuenta administradora inicial y (opcional) datos de ejemplo.
 * Así ya no hace falta importar el .sql a mano con phpMyAdmin.
 */
public final class Bootstrap {

    private static CompletableFuture<Void> ready;

    private Bootstrap() {}

    /** Inicia (una sola vez) la preparación en segundo plano. */
    public static synchronized CompletableFuture<Void> ready() {
        if (ready == null) {
            ready = CompletableFuture.runAsync(Bootstrap::run);
        }
        return ready;
    }

    /** Permite reintentar tras un fallo de conexión. */
    public static synchronized void reset() {
        Database.shutdown();
        ready = null;
    }

    private static void run() {
        try (Connection c = Database.dataSource().getConnection()) {
            for (String sql : script("/db/schema.sql")) {
                try (Statement st = c.createStatement()) {
                    st.execute(sql);
                }
            }
            ensureAdmin(c);
            if (AppConfig.getBoolean("seed.demo", true) && count(c, "living_areas") == 0) {
                for (String sql : script("/db/seed.sql")) {
                    try (Statement st = c.createStatement()) {
                        st.execute(sql);
                    }
                }
            }
        } catch (SQLException | RuntimeException e) {
            Database.shutdown();
            throw new DataAccessException(
                    "No se pudo conectar con la base de datos (" + rootMessage(e) + "). "
                            + "Revisa que MySQL/MariaDB esté encendido y las credenciales en ecogrow.properties.", e);
        }
    }

    private static String rootMessage(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return String.valueOf(t.getMessage());
    }

    private static void ensureAdmin(Connection c) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users WHERE role = ?")) {
            ps.setString(1, Role.ADMIN.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    return;
                }
            }
        }
        String email = AppConfig.get("admin.email", "admin@ecogrow.local");
        String password = AppConfig.get("admin.password", "EcoGrow2026!");
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, "Administrador");
            ps.setString(2, email);
            ps.setString(3, PasswordHasher.hash(password));
            ps.setString(4, Role.ADMIN.name());
            ps.executeUpdate();
        }
        System.out.println("[EcoGrow] Cuenta administradora creada: " + email + " (cambia la contraseña al entrar)");
    }

    private static int count(Connection c, String table) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    /** Lee un script SQL del classpath: ignora comentarios "--" y separa por ";" al final de línea. */
    private static List<String> script(String resource) {
        try (InputStream in = Bootstrap.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Falta el recurso " + resource);
            }
            String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            StringBuilder cleaned = new StringBuilder();
            for (String line : text.split("\\R")) {
                if (!line.trim().startsWith("--")) {
                    cleaned.append(line).append('\n');
                }
            }
            List<String> statements = new ArrayList<>();
            for (String part : cleaned.toString().split(";\\s*\\n")) {
                String s = part.trim();
                if (s.endsWith(";")) {
                    s = s.substring(0, s.length() - 1);
                }
                if (!s.isEmpty()) {
                    statements.add(s);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + resource, e);
        }
    }
}
