package com.usta.controllers;

import java.io.IOException;

import com.usta.App;

import javafx.fxml.FXML;

public class FrontPageController {
    @FXML
    private void SwithToLogin() throws IOException {
        App.setRoot("login");
    }
    @FXML
    private void SwithToSingUp() throws IOException {
        App.setRoot("SingUp");
    }
   
}