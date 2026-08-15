package com.usta.utils;

import java.io.IOException;

import com.usta.models.user.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToolBar;

public class Session {
    private static Session instance;
    private User currentUser;

    // Constructor privado para seguir el patrón Singleton
    private Session() {}

    // Método para obtener la instancia única de la clase
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Obtener el usuario actual
    public User getCurrentUser() {
        return currentUser;
    }

    // Establecer el usuario actual
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Limpiar la sesión (para cuando el usuario cierre sesión, por ejemplo)
    public void clearSession() {
        currentUser = null;
    }

    // Cargar el menú según el tipo de usuario
    public void loadMenu(ToolBar menuBar) {
        try {
            FXMLLoader loader = new FXMLLoader();
            String userType = currentUser != null ? currentUser.getUserType() : "DEFAULT";  // Usar el tipo de usuario en lugar del correo

            // Dependiendo del tipo de usuario, cargar el menú correspondiente
            switch (userType) {
                case "ADMIN":
                    loader.setLocation(getClass().getResource("/com/usta/views/menu/AdminMenu.fxml"));
                    break;
                case "USER":
                    loader.setLocation(getClass().getResource("/com/usta/views/menu/UserMenu.fxml"));
                    break;
                default:
                    loader.setLocation(getClass().getResource("/com/usta/views/menu/UserMenu.fxml"));
            }

            // Cargar el menú y actualizar la barra de herramientas con los nuevos elementos
            ToolBar menu = loader.load();
            menuBar.getItems().setAll(menu.getItems()); // Reemplazar los elementos del menú actual con los nuevos cargados

        } catch (IOException e) {
            e.printStackTrace();  // Imprimir el error si ocurre una excepción
        }
    }
}
