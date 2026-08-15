package com.usta.controllers;

import java.io.IOException;

import com.usta.App;
import com.usta.facade.UserFacade;
import com.usta.models.user.User;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class SingUpController {

    private UserFacade userFacade;

    public SingUpController() {
        this.userFacade = new UserFacade();
    }

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> userTypeBox;

    @FXML
    private TextField documentField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        userTypeBox.setItems(FXCollections.observableArrayList("environmental authority", "owners"));
        userTypeBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSave() throws IOException {
        String name = nameField.getText();
        String email = emailField.getText();
        String userType = userTypeBox.getValue();
        String document = documentField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || userType == null || document.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Registry Error", "All fields are required.", "Please complete all fields.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            mostrarAlerta("Invalid Email", "The email format is incorrect.", "Please provide a valid email address.");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(userType);
        user.setDocument(document);
        user.setPhone(phone);

        try {
            userFacade.registerUser(user);
            mostrarAlerta("Successful Registration", "Successfully registered user.", "Now you can log in.");
            clearFields();
            App.setRoot("login");
        } catch (Exception e) {
            mostrarAlerta("Registry Error", "The email is already registered or there was a database error.", "Please try again.");
        }
    }

    @FXML
    private void handleCancel() throws IOException {
        App.setRoot("FrontPage");
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        documentField.clear();
        phoneField.clear();
        passwordField.clear();
        userTypeBox.getSelectionModel().selectFirst();
    }
}
