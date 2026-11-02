package com.usta.ecogrow.controllers;

import com.usta.ecogrow.model.User;
import com.usta.ecogrow.ui.Dialogs;
import com.usta.ecogrow.ui.Icons;
import com.usta.ecogrow.ui.Navigator;
import com.usta.ecogrow.ui.Session;
import com.usta.ecogrow.ui.Toast;
import com.usta.ecogrow.util.Errors;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/** Marco de la aplicación: barra lateral + encabezado + zona de contenido. */
public class ShellController {

    private record NavItem(String title, String icon, String view, Predicate<Session> visible) {}

    private static final List<NavItem> ITEMS = List.of(
            new NavItem("Panel general", "dashboard", "dashboard", s -> true),
            new NavItem("Áreas de vida", "place", "living-areas", s -> true),
            new NavItem("Taxonomía", "park", "tree-species", s -> true),
            new NavItem("Plantaciones", "drop", "planting-activities", s -> true),
            new NavItem("Censos forestales", "chart", "forest-censuses", s -> true),
            new NavItem("Documentación", "doc", "documentation", s -> true),
            new NavItem("Usuarios", "group", "users", Session::isAdmin));

    @FXML private VBox navBox;
    @FXML private StackPane content;
    @FXML private Label pageTitle;
    @FXML private Label avatarLabel;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private Label modeLabel;
    @FXML private Button themeButton;

    private final Map<NavItem, Button> buttons = new LinkedHashMap<>();

    @FXML
    private void initialize() {
        User user = Session.get().user();
        avatarLabel.setText(user.initials());
        userNameLabel.setText(user.name());
        userRoleLabel.setText(user.role().label());
        modeLabel.setText(Session.get().canWrite() ? "Edición" : "Solo lectura");
        modeLabel.getStyleClass().add(Session.get().canWrite() ? "chip-edit" : "chip-readonly");

        for (NavItem item : ITEMS) {
            if (!item.visible().test(Session.get())) {
                continue;
            }
            Button b = new Button(item.title());
            b.setGraphic(Icons.of(item.icon(), 18));
            b.setMaxWidth(Double.MAX_VALUE);
            b.getStyleClass().add("nav-button");
            b.setOnAction(e -> go(item));
            buttons.put(item, b);
            navBox.getChildren().add(b);
        }
        refreshThemeIcon();
        go(ITEMS.getFirst());
    }

    private void go(NavItem item) {
        try {
            Navigator.Loaded<Object> loaded = Navigator.get().load(item.view());
            loaded.root().setOpacity(0);
            content.getChildren().setAll(loaded.root());
            FadeTransition fade = new FadeTransition(Duration.millis(220), loaded.root());
            fade.setToValue(1);
            fade.play();
            pageTitle.setText(item.title());
            buttons.forEach((i, b) -> {
                b.getStyleClass().remove("active");
                if (i == item) {
                    b.getStyleClass().add("active");
                }
            });
        } catch (RuntimeException e) {
            Toast.error("No se pudo abrir «" + item.title() + "»: " + Errors.message(e));
        }
    }

    @FXML
    private void onToggleTheme() {
        Navigator.get().toggleTheme();
        refreshThemeIcon();
    }

    private void refreshThemeIcon() {
        themeButton.setGraphic(Icons.of(Navigator.get().isDark() ? "sun" : "moon", 18));
    }

    @FXML
    private void onLogout() {
        if (Dialogs.confirm("Cerrar sesión", "¿Quieres salir de tu cuenta?", "Cerrar sesión")) {
            Session.get().end();
            Navigator.get().show("login");
        }
    }
}
