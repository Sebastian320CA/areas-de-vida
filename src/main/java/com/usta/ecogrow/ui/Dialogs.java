package com.usta.ecogrow.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public final class Dialogs {

    private Dialogs() {}

    public static boolean confirm(String title, String message, String confirmText) {
        ButtonType ok = new ButtonType(confirmText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ok, cancel);
        alert.setTitle("EcoGrow");
        alert.setHeaderText(title);
        alert.setGraphic(null);
        alert.initOwner(Navigator.get().stage());
        Navigator.get().style(alert.getDialogPane());
        return alert.showAndWait().filter(b -> b == ok).isPresent();
    }
}
