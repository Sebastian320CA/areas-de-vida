package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.facade.UserFacade;
import com.usta.ecogrow.model.Role;
import com.usta.ecogrow.model.User;
import com.usta.ecogrow.ui.Badges;
import com.usta.ecogrow.ui.Form;
import com.usta.ecogrow.ui.Session;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

/** Administración de usuarios (solo visible para el rol ADMIN). */
public class UsersController extends CrudController<User> {

    private final UserFacade facade = new UserFacade();
    private String pendingPassword = "";

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Role> roleBox;
    @FXML private TextField documentField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;

    @Override protected CrudDao<User> dao() { return facade.dao(); }
    @Override protected String entityName() { return "usuario"; }
    @Override protected int idOf(User u) { return u.id(); }
    @Override protected String describe(User u) { return "Se eliminará la cuenta de " + u.name() + " (" + u.email() + ")."; }

    @Override
    protected void loadExtras() {
        roleBox.getItems().setAll(Role.values());
    }

    @Override
    protected void configureColumns() {
        column("Nombre", User::name, 180);
        column("Correo", User::email, 220);
        TableColumn<User, Role> role = column("Rol", User::role, 160);
        role.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Role r, boolean empty) {
                super.updateItem(r, empty);
                setText(null);
                setGraphic(empty || r == null ? null : Badges.of(r));
            }
        });
        column("Documento", u -> u.document().isBlank() ? "—" : u.document(), 120);
        column("Teléfono", u -> u.phone().isBlank() ? "—" : u.phone(), 120);
    }

    @Override
    protected void fillForm(User u) {
        nameField.setText(u.name());
        emailField.setText(u.email());
        roleBox.setValue(u.role());
        documentField.setText(u.document());
        phoneField.setText(u.phone());
        passwordField.clear();
        passwordField.setPromptText("Dejar vacío para no cambiarla");
    }

    @Override
    protected void clearForm() {
        Form.reset(nameField, emailField, roleBox, documentField, phoneField, passwordField);
        roleBox.setValue(Role.OWNER);
        passwordField.setPromptText("Mínimo 8 caracteres, letras y números");
    }

    @Override
    protected User readForm() {
        String name = Form.required(nameField, "Nombre");
        String email = Form.required(emailField, "Correo");
        Role role = Form.choice(roleBox, "Rol");
        pendingPassword = selected == null ? Form.required(passwordField, "Contraseña") : Form.optional(passwordField);
        return new User(currentId(), name, email, "", role, Form.optional(documentField), Form.optional(phoneField));
    }

    @Override
    protected User persist(User item) {
        return facade.saveAsAdmin(item, pendingPassword);
    }

    @Override
    protected void remove(User item) {
        facade.deleteAsAdmin(item, Session.get().user());
    }

    @Override
    protected boolean matches(User u, String q) {
        return u.name().toLowerCase().contains(q) || u.email().toLowerCase().contains(q)
                || u.role().label().toLowerCase().contains(q);
    }
}
