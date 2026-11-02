package com.usta.ecogrow.controllers;

import com.usta.ecogrow.db.Bootstrap;
import com.usta.ecogrow.facade.UserFacade;
import com.usta.ecogrow.ui.Layout;
import com.usta.ecogrow.ui.Navigator;
import com.usta.ecogrow.ui.Session;
import com.usta.ecogrow.util.Async;
import com.usta.ecogrow.util.Errors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class LoginController {

    private final UserFacade facade = new UserFacade();

    @FXML private StackPane hero;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label messageLabel;
    @FXML private Hyperlink retryLink;

    @FXML
    private void initialize() {
        Layout.clipToBounds(hero);
        connect();
    }

    private void connect() {
        loginButton.setDisable(true);
        retryLink.setVisible(false);
        retryLink.setManaged(false);
        showMessage("Conectando con la base de datos…", "info");

        Bootstrap.ready().whenComplete((ok, error) -> Platform.runLater(() -> {
            if (error == null) {
                loginButton.setDisable(false);
                hideMessage();
                emailField.requestFocus();
            } else {
                showMessage(Errors.message(error), "error");
                retryLink.setVisible(true);
                retryLink.setManaged(true);
            }
        }));
    }

    @FXML
    private void onRetry() {
        Bootstrap.reset();
        connect();
    }

    @FXML
    private void onLogin() {
        if (loginButton.isDisabled()) {
            return;
        }
        String email = emailField.getText();
        String password = passwordField.getText();
        loginButton.setDisable(true);
        loginButton.setText("Ingresando…");
        hideMessage();

        Async.run(() -> facade.login(email, password), user -> {
            Session.get().start(user);
            Navigator.get().show("shell");
        }, error -> {
            loginButton.setDisable(false);
            loginButton.setText("Iniciar sesión");
            passwordField.clear();
            passwordField.requestFocus();
            showMessage(Errors.message(error), "error");
        });
    }

    @FXML
    private void onSignUp() {
        Navigator.get().show("signup");
    }

    private void showMessage(String text, String kind) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().removeAll("msg-error", "msg-info");
        messageLabel.getStyleClass().add("msg-" + kind);
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    private void hideMessage() {
        messageLabel.setVisible(false);
        messageLabel.setManaged(false);
    }
}
