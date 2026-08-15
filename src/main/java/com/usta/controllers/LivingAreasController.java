package com.usta.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.usta.App;
import com.usta.models.livingAreas.LivingAreas;
import com.usta.models.livingAreas.LivingAreasImplDAO;
import com.usta.utils.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import javafx.event.ActionEvent;

public class LivingAreasController {
    @FXML
    private void switchtoleave() throws IOException {
        App.setRoot("FrontPage");  
    }
    @FXML
    private void switchtoreturn() throws IOException {
        App.setRoot("Dashboard");  
    }

    @FXML private TextField locationTextField;
    @FXML private TextField sizeTextField;
    @FXML private TextField conservationStatusTextField;
    @FXML private DatePicker creationDatePicker;
    @FXML private TableView<LivingAreas> livingAreasTable;
    @FXML private TableColumn<LivingAreas, Integer> idColumn;
    @FXML private TableColumn<LivingAreas, String> locationColumn;
    @FXML private TableColumn<LivingAreas, Double> sizeColumn;
    @FXML private TableColumn<LivingAreas, String> conservationStatusColumn;
    @FXML private TableColumn<LivingAreas, Date> creationDateColumn;
    @FXML private Button createButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ObservableList<LivingAreas> livingAreasList;
    private LivingAreasImplDAO dao;
    private boolean isEditing = false;
    private LivingAreas currentlyEditing = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty().asObject());
        conservationStatusColumn.setCellValueFactory(cellData -> cellData.getValue().conservationStatusProperty());
        creationDateColumn.setCellValueFactory(cellData -> cellData.getValue().creationDateProperty());

        livingAreasList = FXCollections.observableArrayList();
        Connection connection = ConnectionManager.getInstance().getConnection();
        dao = LivingAreasImplDAO.getInstance(connection);
        loadLivingAreas();

        livingAreasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void populateFields(LivingAreas area) {
        locationTextField.setText(area.getLocation());
        sizeTextField.setText(String.valueOf(area.getSize()));
        conservationStatusTextField.setText(area.getConservationStatus());
        creationDatePicker.setValue(area.getCreationDate().toLocalDate());
    }

    private void clearFields() {
        locationTextField.clear();
        sizeTextField.clear();
        conservationStatusTextField.clear();
        creationDatePicker.setValue(null);
        isEditing = false;
        currentlyEditing = null;
    }

    private boolean validateFields() {
        if (locationTextField.getText().isEmpty() ||
            sizeTextField.getText().isEmpty() ||
            conservationStatusTextField.getText().isEmpty() ||
            creationDatePicker.getValue() == null) {
            showError("All fields are required.");
            return false;
        }
        try {
            Double.parseDouble(sizeTextField.getText());
        } catch (NumberFormatException e) {
            showError("Size must be a valid number.");
            return false;
        }
        return true;
    }

    @FXML
    public void createLivingArea(@SuppressWarnings("exports") ActionEvent event) {
        try {
            if (validateFields()) {
                LivingAreas newArea = new LivingAreas(
                    0,
                    locationTextField.getText(),
                    Double.parseDouble(sizeTextField.getText()),
                    conservationStatusTextField.getText(),
                    Date.valueOf(creationDatePicker.getValue())
                );
                dao.createLivingArea(newArea);
                loadLivingAreas();
                clearFields();
            }
        } catch (SQLException e) {
            showError("Error creating area: " + e.getMessage());
        }
    }

    @FXML
    public void editLivingArea(@SuppressWarnings("exports") ActionEvent event) {
        if (!isEditing) {
            LivingAreas selectedArea = livingAreasTable.getSelectionModel().getSelectedItem();
            if (selectedArea == null) {
                showError("Please select an area to edit.");
                return;
            }
            currentlyEditing = selectedArea;
            isEditing = true;
            editButton.setText("SAVE");
            populateFields(selectedArea);
        } else {
            try {
                if (validateFields() && currentlyEditing != null) {
                    currentlyEditing.setLocation(locationTextField.getText());
                    currentlyEditing.setSize(Double.parseDouble(sizeTextField.getText()));
                    currentlyEditing.setConservationStatus(conservationStatusTextField.getText());
                    currentlyEditing.setCreationDate(Date.valueOf(creationDatePicker.getValue()));
                    
                    dao.updateLivingArea(currentlyEditing);
                    loadLivingAreas();
                    clearFields();
                    editButton.setText("EDIT");
                }
            } catch (SQLException e) {
                showError("Error updating area: " + e.getMessage());
            }
        }
    }

    @FXML
    public void deleteLivingArea(@SuppressWarnings("exports") ActionEvent event) {
        LivingAreas selectedArea = livingAreasTable.getSelectionModel().getSelectedItem();
        if (selectedArea != null) {
            try {
                dao.deleteLivingArea(selectedArea.getId());
                loadLivingAreas();
                clearFields();
            } catch (SQLException e) {
                showError("Error deleting area: " + e.getMessage());
            }
        } else {
            showError("Please select an area to remove.");
        }
    }

    public void loadLivingAreas() {
        try {
            livingAreasList.setAll(dao.getAllLivingAreas());
            livingAreasTable.setItems(livingAreasList);
        } catch (SQLException e) {
            showError("Error loading areas: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
