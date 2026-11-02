package com.usta.ecogrow.ui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Notificaciones no bloqueantes (reemplazan a los Alert modales de la versión anterior). */
public final class Toast {

    private Toast() {}

    public static void success(String message) {
        show(message, "success", "ok");
    }

    public static void error(String message) {
        show(message, "error", "error");
    }

    public static void info(String message) {
        show(message, "info", "ok");
    }

    private static void show(String message, String kind, String icon) {
        VBox layer = Navigator.get().toastLayer();

        Label text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(340);
        text.getStyleClass().add("toast-text");

        HBox toast = new HBox(10, Icons.of(icon, 20), text);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.getStyleClass().addAll("toast", "toast-" + kind);
        toast.setMaxWidth(420);
        layer.getChildren().add(toast);

        FadeTransition in = new FadeTransition(Duration.millis(180), toast);
        in.setFromValue(0);
        in.setToValue(1);
        PauseTransition stay = new PauseTransition(Duration.seconds("error".equals(kind) ? 5 : 3));
        FadeTransition out = new FadeTransition(Duration.millis(320), toast);
        out.setToValue(0);
        SequentialTransition seq = new SequentialTransition(in, stay, out);
        seq.setOnFinished(e -> layer.getChildren().remove(toast));
        seq.play();
    }
}
