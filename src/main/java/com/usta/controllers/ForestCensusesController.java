package com.usta.controllers;

import com.usta.models.forestCensuses.ForestCensuses;
import com.usta.models.forestCensuses.ForestCensusesImplDAO;
import com.usta.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import com.usta.App; 

public class ForestCensusesController {

    @FXML
    private TableView<ForestCensuses> tableCensuses;
    @FXML
    private TableColumn<ForestCensuses, Integer> colId;
    @FXML
    private TableColumn<ForestCensuses, LocalDate> colCensusDate;
    @FXML
    private TableColumn<ForestCensuses, Double> colVegetationCoverage;
    @FXML
    private TableColumn<ForestCensuses, String> colEcosystemState;
    @FXML
    private TableColumn<ForestCensuses, String> colObservations;
    @FXML
    private DatePicker txtCensusDate;
    @FXML
    private TextField txtVegetationCoverage;
    @FXML
    private TextField txtEcosystemState;
    @FXML
    private TextField txtObservations;

    private ForestCensusesImplDAO forestCensusesDAO;

    public void initialize() {
        Connection connection = ConnectionManager.getInstance().getConnection();
        forestCensusesDAO = new ForestCensusesImplDAO(connection);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCensusDate.setCellValueFactory(new PropertyValueFactory<>("censusDate"));
        colVegetationCoverage.setCellValueFactory(new PropertyValueFactory<>("vegetationCoverage"));
        colEcosystemState.setCellValueFactory(new PropertyValueFactory<>("ecosystemState"));
        colObservations.setCellValueFactory(new PropertyValueFactory<>("observations"));
        loadForestCensuses();

        tableCensuses.setOnMouseClicked(this::handleTableSelection);
    }

    private void loadForestCensuses() {
        try {
            List<ForestCensuses> censuses = forestCensusesDAO.getAll();
            ObservableList<ForestCensuses> observableCensuses = FXCollections.observableArrayList(censuses);
            tableCensuses.setItems(observableCensuses);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error loading forest censuses", e.getMessage());
        }
    }

    @FXML
    private void onCreate(ActionEvent event) {
        try {
            if (txtCensusDate.getValue() == null || txtVegetationCoverage.getText().isEmpty() || 
                txtEcosystemState.getText().isEmpty() || txtObservations.getText().isEmpty()) {
                showAlert(AlertType.WARNING, "Incomplete Fields", "Please fill all the fields", null);
                return;
            }

            LocalDate censusDate = txtCensusDate.getValue();
            double vegetationCoverage = Double.parseDouble(txtVegetationCoverage.getText());
            String ecosystemState = txtEcosystemState.getText();
            String observations = txtObservations.getText();

            ForestCensuses newCensus = new ForestCensuses(0, censusDate, vegetationCoverage, ecosystemState, observations);
            forestCensusesDAO.add(newCensus);
            loadForestCensuses();
            clearFields();

            showAlert(AlertType.INFORMATION, "Census Created", "The new census has been created successfully.", null);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for vegetation coverage", e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "An error occurred while creating the census", e.getMessage());
        }
    }

    @FXML
    private void onEdit(ActionEvent event) {
        ForestCensuses selectedCensus = tableCensuses.getSelectionModel().getSelectedItem();
        if (selectedCensus != null) {
            try {
                if (txtCensusDate.getValue() == null || txtVegetationCoverage.getText().isEmpty() ||
                    txtEcosystemState.getText().isEmpty() || txtObservations.getText().isEmpty()) {
                    showAlert(AlertType.WARNING, "Incomplete Fields", "Please fill all the fields", null);
                    return;
                }

                selectedCensus.setCensusDate(txtCensusDate.getValue());
                selectedCensus.setVegetationCoverage(Double.parseDouble(txtVegetationCoverage.getText()));
                selectedCensus.setEcosystemState(txtEcosystemState.getText());
                selectedCensus.setObservations(txtObservations.getText());
                forestCensusesDAO.update(selectedCensus);
                loadForestCensuses();
                clearFields();

                showAlert(AlertType.INFORMATION, "Census Updated", "The selected census has been updated successfully.", null);
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for vegetation coverage", e.getMessage());
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "An error occurred while editing the census", e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "Please select a census to edit", null);
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        ForestCensuses selectedCensus = tableCensuses.getSelectionModel().getSelectedItem();
        if (selectedCensus != null) {
            try {
                forestCensusesDAO.delete(selectedCensus.getId());
                loadForestCensuses();
                clearFields();

                showAlert(AlertType.INFORMATION, "Census Deleted", "The selected census has been deleted successfully.", null);
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "An error occurred while deleting the census", e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "Please select a census to delete", null);
        }
    }

    @FXML
    private void clearFields() {
        txtCensusDate.setValue(null);
        txtVegetationCoverage.clear();
        txtEcosystemState.clear();
        txtObservations.clear();
    }

    private void handleTableSelection(MouseEvent event) {
        ForestCensuses selectedCensus = tableCensuses.getSelectionModel().getSelectedItem();
        if (selectedCensus != null) {
            txtCensusDate.setValue(selectedCensus.getCensusDate());
            txtVegetationCoverage.setText(String.valueOf(selectedCensus.getVegetationCoverage()));
            txtEcosystemState.setText(selectedCensus.getEcosystemState());
            txtObservations.setText(selectedCensus.getObservations());
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    @FXML
    private void switchToDash(ActionEvent event) {
        try {
            System.out.println("Volviendo al Dashboard...");
            App.setRoot("Dashboard"); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToLeave(ActionEvent event) {
        try {
            System.out.println("Volviendo al FrontPage...");
            App.setRoot("FrontPage"); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
