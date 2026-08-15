package com.usta.controllers;

import java.io.IOException;
import com.usta.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PanelController {

    @FXML
    private Button plantingActivitiesButton;

    @FXML
    private void Switchtoplantingactivities() throws IOException {
        App.setRoot("PlantingActivities");  
    }
    @FXML
    private void ForestCensuses() throws IOException {
        App.setRoot("ForestCensuses");  
    }
   
    @FXML
    private void Livingareas() throws IOException {
        App.setRoot("LivingAreas");   
    }
    @FXML
    private void Treetaxonomy() throws IOException {
        App.setRoot("tree");  
    }
    @FXML
    private void FrontPage() throws IOException {
        App.setRoot("FrontPage");  
    }
    
}
