package com.usta.controllers;

import java.io.IOException;
import com.usta.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DashBoardController {

    @FXML
    private Button plantingActivitiesButton;

    @FXML
    private void handleGoToPlantingActivities() throws IOException {
        App.setRoot("PlantingActivities");  
    }
    @FXML
    private void SwitchToForestCensuses() throws IOException {
        App.setRoot("ForestCensuses");  
    }
    @FXML
    private void SwitchToDocumentation() throws IOException {
        App.setRoot("Documentation");  
    }
    @FXML
    private void SwitchToLivingAreas() throws IOException {
        App.setRoot("LivingAreas");   
    }
    @FXML
    private void SwitchToTreeTaxonomy() throws IOException {
        App.setRoot("tree");  
    }
    @FXML
    private void SwitchToFront() throws IOException {
        App.setRoot("FrontPage");  
    }
}
