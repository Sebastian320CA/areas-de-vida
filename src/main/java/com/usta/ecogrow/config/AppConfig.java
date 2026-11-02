package com.usta.ecogrow.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Configuración centralizada. Prioridad: variable de entorno > ecogrow.properties
 * externo (carpeta de trabajo) > ecogrow.properties embebido.
 */
public final class AppConfig {

    private static final Properties PROPS = load();

    private AppConfig() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = AppConfig.class.getResourceAsStream("/ecogrow.properties")) {
            if (in != null) {
                p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
            // se usan los valores por defecto del código
        }
        Path external = Path.of("ecogrow.properties");
        if (Files.isRegularFile(external)) {
            try (Reader r = Files.newBufferedReader(external, StandardCharsets.UTF_8)) {
                p.load(r);
            } catch (IOException ignored) {
                // idem
            }
        }
        return p;
    }

    public static String get(String key, String fallback) {
        String env = System.getenv("ECOGROW_" + key.toUpperCase().replace('.', '_'));
        if (env != null && !env.isBlank()) {
            return env;
        }
        return PROPS.getProperty(key, fallback);
    }

    public static boolean getBoolean(String key, boolean fallback) {
        return Boolean.parseBoolean(get(key, String.valueOf(fallback)));
    }
}
