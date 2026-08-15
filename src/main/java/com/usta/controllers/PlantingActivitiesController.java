package com.usta.controllers;

import com.usta.App;
import com.usta.models.plantingActivities.PlantingActivities;
import com.usta.models.plantingActivities.PlantingActivitiesImplDAO;
import com.usta.utils.ConnectionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PlantingActivitiesController {
    @FXML
    private void switchtoreturn() throws IOException {
        App.setRoot("Dashboard");  
    }

    @FXML
    private void switchtofront() throws IOException {
        App.setRoot("FrontPage");  
    }

    @FXML
    private TableView<PlantingActivities> plantingActivitiesTable;
    @FXML
    private TableColumn<PlantingActivities, Integer> idColumn;
    @FXML
    private TableColumn<PlantingActivities, LocalDate> activityDateColumn;
    @FXML
    private TableColumn<PlantingActivities, String> activityTypeColumn;
    @FXML
    private TableColumn<PlantingActivities, String> descriptionColumn;
    @FXML
    private TableColumn<PlantingActivities, Integer> livingAreaColumn;

    @FXML
    private TextField activityTypeField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker activityDatePicker;
    @FXML
    private TextField livingAreaField;
    @FXML
    private TextField searchField;

    private PlantingActivitiesImplDAO plantingActivitiesDAO;

    public void initialize() {
        Connection connection = ConnectionManager.getInstance().getConnection();
        plantingActivitiesDAO = new PlantingActivitiesImplDAO(connection);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        activityDateColumn.setCellValueFactory(new PropertyValueFactory<>("activityDate"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        livingAreaColumn.setCellValueFactory(new PropertyValueFactory<>("livingAreaId"));

        loadPlantingActivities();

        plantingActivitiesTable.setOnMouseClicked(this::handleTableSelection);
    }

    private void loadPlantingActivities() {
        try {
            List<PlantingActivities> activities = plantingActivitiesDAO.getAll();
            ObservableList<PlantingActivities> observableActivities = FXCollections.observableArrayList(activities);
            plantingActivitiesTable.setItems(observableActivities);
        } catch (SQLException e) {
            System.out.println("Error loading activities: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
private void addPlantingActivity() {
    if (validateFields()) {
        try {
            int livingAreaId = Integer.parseInt(livingAreaField.getText());

            // Verificar si el livingAreaId es válido
            if (!plantingActivitiesDAO.isLivingAreaIdValid(livingAreaId)) {
                System.out.println("Error: Living Area ID does not exist.");
                showAlert(AlertType.ERROR, "Invalid Living Area ID", "The specified Living Area ID does not exist. Please enter a valid ID.", null);
                return;
            }

            PlantingActivities newActivity = new PlantingActivities(
                0,
                activityDatePicker.getValue(),
                activityTypeField.getText(),
                descriptionField.getText(),
                livingAreaId
            );

            plantingActivitiesDAO.add(newActivity);
            loadPlantingActivities();
            clearFields();
            System.out.println("Activity added successfully.");
            showAlert(AlertType.INFORMATION, "Success", "The planting activity has been added successfully.", null);
        } catch (NumberFormatException e) {
            System.out.println("Living Area ID must be a number.");
            showAlert(AlertType.ERROR, "Invalid Input", "Living Area ID must be a valid number.", e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error adding activity: " + e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while adding the activity: " + e.getMessage(), null);
        }
    }
}

    @FXML
    private void editPlantingActivity() {
        PlantingActivities selectedActivity = plantingActivitiesTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null && validateFields()) {
            try {
                selectedActivity.setActivityType(activityTypeField.getText());
                selectedActivity.setDescription(descriptionField.getText());
                selectedActivity.setActivityDate(activityDatePicker.getValue());
                selectedActivity.setLivingAreaId(Integer.parseInt(livingAreaField.getText()));

                plantingActivitiesDAO.update(selectedActivity);
                loadPlantingActivities();
                clearFields();
                System.out.println("Activity updated successfully.");
            } catch (NumberFormatException e) {
                System.out.println("Living Area ID must be a number.");
            } catch (SQLException e) {
                System.out.println("Error updating activity:" + e.getMessage());
            }
        } else {
            System.out.println("Select an activity to edit.");
        }
    }

    @FXML
    private void deletePlantingActivity() {
        PlantingActivities selectedActivity = plantingActivitiesTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            try {
                plantingActivitiesDAO.delete(selectedActivity.getId());
                loadPlantingActivities();
                clearFields();
                System.out.println("Activity deleted successfully.");
            } catch (SQLException e) {
                System.out.println("Error deleting activity: " + e.getMessage());
            }
        } else {
            System.out.println("Select an activity to delete.");
        }
    }

    @FXML
    private void filterPlantingActivities() {
        String searchText = searchField.getText().toLowerCase();
        try {
            List<PlantingActivities> allActivities = plantingActivitiesDAO.getAll();
            List<PlantingActivities> filteredActivities = allActivities.stream()
                .filter(activity -> activity.getActivityType().toLowerCase().contains(searchText) || 
                                    activity.getDescription().toLowerCase().contains(searchText))
                .toList();
            plantingActivitiesTable.setItems(FXCollections.observableArrayList(filteredActivities));
        } catch (SQLException e) {
            System.out.println("Error when filtering activities: " + e.getMessage());
        }
    }

    @FXML
    private void clearFields() {
        activityTypeField.clear();
        descriptionField.clear();
        activityDatePicker.setValue(null);
        livingAreaField.clear();
    }

    private void handleTableSelection(MouseEvent event) {
        PlantingActivities selectedActivity = plantingActivitiesTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            activityTypeField.setText(selectedActivity.getActivityType());
            descriptionField.setText(selectedActivity.getDescription());
            activityDatePicker.setValue(selectedActivity.getActivityDate());
            livingAreaField.setText(String.valueOf(selectedActivity.getLivingAreaId()));
        }
    }

    private boolean validateFields() {
        if (activityTypeField.getText().isEmpty() ||
            descriptionField.getText().isEmpty() ||
            activityDatePicker.getValue() == null ||
            livingAreaField.getText().isEmpty()) {
            System.out.println("Please complete all fields.");
            return false;
        }
        return true;
    }
}
