package com.usta.ecogrow.ui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * Singleton que centraliza la navegación entre vistas FXML, el tema claro/oscuro
 * y la capa donde se muestran las notificaciones (toasts).
 */
public final class Navigator {

    public record Loaded<C>(Parent root, C controller) {}

    private static final Navigator INSTANCE = new Navigator();
    private static final String VIEWS = "/com/usta/ecogrow/views/";

    private final StackPane host = new StackPane();
    private final StackPane viewLayer = new StackPane();
    private final VBox toastLayer = new VBox(10);
    private final Preferences prefs = Preferences.userNodeForPackage(Navigator.class);

    private Stage stage;
    private boolean dark;

    private Navigator() {}

    public static Navigator get() {
        return INSTANCE;
    }

    public void init(Stage primary) {
        this.stage = primary;

        host.getStyleClass().add("eco-root");
        toastLayer.setAlignment(Pos.BOTTOM_RIGHT);
        toastLayer.setPadding(new Insets(24));
        toastLayer.setPickOnBounds(false);
        toastLayer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        StackPane.setAlignment(toastLayer, Pos.BOTTOM_RIGHT);
        host.getChildren().addAll(viewLayer, toastLayer);

        Scene scene = new Scene(host, 1280, 800);
        scene.getStylesheets().add(stylesheet());

        dark = prefs.getBoolean("dark", false);
        applyTheme();

        primary.setScene(scene);
        primary.setTitle("EcoGrow");
        primary.setMinWidth(1040);
        primary.setMinHeight(680);
    }

    public Stage stage() {
        return stage;
    }

    public String stylesheet() {
        return Objects.requireNonNull(Navigator.class.getResource("/com/usta/ecogrow/css/ecogrow.css"),
                "No se encontró ecogrow.css").toExternalForm();
    }

    /** Reemplaza toda la ventana por la vista indicada (login, registro, shell). */
    public <C> C show(String view) {
        Loaded<C> loaded = load(view);
        viewLayer.getChildren().setAll(loaded.root());
        return loaded.controller();
    }

    /** Carga un FXML sin mostrarlo (lo usa el shell para el contenido interno). */
    public <C> Loaded<C> load(String view) {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                Navigator.class.getResource(VIEWS + view + ".fxml"), "No existe la vista " + view));
        try {
            Parent root = loader.load();
            Icons.decorate(root);
            return new Loaded<>(root, loader.getController());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar la vista " + view, e);
        }
    }

    // ---- tema ----

    public boolean isDark() {
        return dark;
    }

    public void toggleTheme() {
        dark = !dark;
        prefs.putBoolean("dark", dark);
        applyTheme();
    }

    private void applyTheme() {
        host.getStyleClass().remove("dark");
        if (dark) {
            host.getStyleClass().add("dark");
        }
    }

    /** Aplica el estilo actual a un cuadro de diálogo (que vive en otra ventana). */
    public void style(javafx.scene.control.DialogPane pane) {
        pane.getStylesheets().add(stylesheet());
        pane.getStyleClass().add("eco-root");
        if (dark) {
            pane.getStyleClass().add("dark");
        }
    }

    VBox toastLayer() {
        return toastLayer;
    }
}
