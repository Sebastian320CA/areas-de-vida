package com.usta.controllers;

import java.io.IOException;
import com.usta.App;
import com.usta.facade.UserFacade;
import com.usta.models.user.User;
import com.usta.utils.Window;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    private final UserFacade userFacade = new UserFacade();

    @FXML
    private TextField EmailField;

    @FXML
    private PasswordField PassField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button returnButton;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
private void initialize() {

    comboBox.getItems().addAll("Authority Ambient", "Owner");
    comboBox.setOnAction(event -> handleComboBoxSelection());

    loginButton.setVisible(false);
    registerButton.setVisible(false);
}


    @FXML
    private void handleComboBoxSelection() {
        String selectedOption = comboBox.getValue();

        if ("Owner".equals(selectedOption)) {
            loginButton.setVisible(false);  
            registerButton.setVisible(true); 
        } else if ("Authority Ambient".equals(selectedOption)) {
            loginButton.setVisible(true); 
            registerButton.setVisible(false); 
        }
    }

    @FXML
    private void handleReturn() throws IOException {
        App.setRoot("FrontPage");
    }
    @FXML
    private void handlePanel() throws IOException {
        App.setRoot("Panel");
        @SuppressWarnings("unused")
        String email = EmailField.getText();
        @SuppressWarnings("unused")
        String password = PassField.getText();
    }

    @FXML
    private void handleLogin() {
        String email = EmailField.getText();
        String password = PassField.getText();
        try {
            User authenticatedUser = userFacade.loginUser(email, password);
            if (authenticatedUser != null) {
                App.setRoot("Dashboard");
            } else {
                new Window("Authentication Error",
                        "Incorrect email or password.",
                        "Please verify your credentials.",
                        AlertType.WARNING);
            }
        } catch (Exception e) {
            new Window("Error",
                    "An error occurred during authentication.",
                    "Please try again later. " + e.getMessage(),
                    AlertType.ERROR);
        } finally {
            EmailField.clear();
            PassField.clear();
        }
    }

    @FXML
    private void handleRegister() {
        String email = EmailField.getText();
        String password = PassField.getText();
        String userType = comboBox.getValue();
        try {
            if (email.isEmpty() || password.isEmpty() || userType == null) {
                new Window("Registration Error",
                        "All fields are required.",
                        "Please fill in all fields.",
                        AlertType.WARNING);
                return;
            }

            User newUser = new User(email, email, password, userType, "", "");
            userFacade.registerUser(newUser);
            new Window("Registration Successful",
                    "User registered successfully.",
                    "You can now log in with your credentials.",
                    AlertType.INFORMATION);
        } catch (Exception e) {
            new Window("Error",
                    "An error occurred during registration.",
                    "Please try again later. " + e.getMessage(),
                    AlertType.ERROR);
        } finally {
            EmailField.clear();
            PassField.clear();
        }
    }
}
 