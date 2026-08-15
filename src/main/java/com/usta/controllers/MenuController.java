package com.usta.controllers;

import java.io.IOException;

import com.usta.App;

import javafx.fxml.FXML;

public class MenuController {
    @FXML
    private void SwithcToLivingAreas() throws IOException {
        App.setRoot("LivingAreas");
    }
    @FXML
    private void SwitchToForestCensuses() throws IOException {
        App.setRoot("ForestCensuses");
    }
    @FXML
    private void SwitchToPlantingActivities() throws IOException {
        App.setRoot("PlantingActivities");
    }
    @FXML
    private void SwitchToDocumentation() throws IOException {
        App.setRoot("Documentation");
    }
    @FXML
    private void SwitchToOwners() throws IOException {
        App.setRoot("Owners");
    }
}
