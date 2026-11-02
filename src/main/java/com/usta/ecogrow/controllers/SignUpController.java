package com.usta.ecogrow.controllers;

import com.usta.ecogrow.facade.UserFacade;
import com.usta.ecogrow.model.Role;
import com.usta.ecogrow.ui.Form;
import com.usta.ecogrow.ui.Layout;
import com.usta.ecogrow.ui.Navigator;
import com.usta.ecogrow.ui.Toast;
import com.usta.ecogrow.util.Async;
import com.usta.ecogrow.util.Errors;
import com.usta.ecogrow.util.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class SignUpController {

    private final UserFacade facade = new UserFacade();

    @FXML private StackPane background;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Role> roleBox;
    @FXML private TextField documentField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Button createButton;

    @FXML
    private void initialize() {
        Layout.clipToBounds(background);
        roleBox.getItems().setAll(Role.AUTHORITY, Role.OWNER);
        roleBox.setValue(Role.OWNER);
    }

    @FXML
    private void onCreate() {
        String name;
        String email;
        Role role;
        String document;
        String phone;
        String password;
        try {
            name = Form.required(nameField, "Nombre completo");
            email = Form.required(emailField, "Correo electrónico");
            role = Form.choice(roleBox, "Tipo de usuario");
            document = Form.required(documentField, "Documento");
            phone = Form.required(phoneField, "Teléfono");
            password = Form.required(passwordField, "Contraseña");
            if (!password.equals(confirmField.getText())) {
                throw new ValidationException("Las contraseñas no coinciden.");
            }
        } catch (ValidationException e) {
            Toast.error(e.getMessage());
            return;
        }

        createButton.setDisable(true);
        Async.run(() -> facade.register(name, email, password, role, document, phone), user -> {
            Navigator.get().show("login");
            Toast.success("Cuenta creada. Ya puedes iniciar sesión.");
        }, error -> {
            createButton.setDisable(false);
            Toast.error(Errors.message(error));
        });
    }

    @FXML
    private void onBack() {
        Navigator.get().show("login");
    }
}
