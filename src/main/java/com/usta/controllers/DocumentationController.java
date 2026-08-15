package com.usta.controllers;

import com.usta.App;
import com.usta.models.documentations.Documentation;
import com.usta.models.documentations.DocumentationImplDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class DocumentationController {

    @FXML private TableView<Documentation> documentationTable;
    @FXML private TableColumn<Documentation, Integer> idColumn;
    @FXML private TableColumn<Documentation, String> documentTypeColumn;
    @FXML private TableColumn<Documentation, String> documentNumberColumn;
    @FXML private TableColumn<Documentation, Date> issueDateColumn;
    @FXML private TableColumn<Documentation, String> issuingAuthorityColumn;

    @FXML private TextField documentTypeField;
    @FXML private TextField documentNumberField;
    @FXML private DatePicker issueDatePicker;
    @FXML private TextField issuingAuthorityField;

    @FXML
    private void SwithToPanel() throws IOException {
        App.setRoot("Panel");
    }
    @FXML
    private void SwitchToFront() throws IOException {
        App.setRoot("FrontPage");
    }


    private final DocumentationImplDAO documentationDAO;

    public DocumentationController() {
        this.documentationDAO = new DocumentationImplDAO();
    }

    @FXML
    public void initialize() {
        try {
            List<Documentation> documentationList = documentationDAO.getAllDocumentation();
            ObservableList<Documentation> documentationObservableList = FXCollections.observableArrayList(documentationList);
            documentationTable.setItems(documentationObservableList);
    
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            documentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("documentType"));
            documentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));
            issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
            issuingAuthorityColumn.setCellValueFactory(new PropertyValueFactory<>("issuingAuthority"));
        } catch (SQLException e) {
            showAlert("Mistake", "Failed to initialize table: " + e.getMessage());
        }
    }

    @FXML
    public void handleAdd() {
        try {
            if (!validateFields()) return;

            Documentation documentation = new Documentation(
                    0,
                    documentTypeField.getText(),
                    documentNumberField.getText(),
                    Date.valueOf(issueDatePicker.getValue()),
                    issuingAuthorityField.getText(),
                    null 
            );

            documentationDAO.insertDocumentation(documentation);
            showAlert("success", "Document added successfully.");
            refreshTable();

        } catch (SQLException e) {
            showAlert("Mistake", "Failed to add document: " + e.getMessage());
        }
    }

    @FXML
    public void handleEdit() {
        Documentation selectedDoc = documentationTable.getSelectionModel().getSelectedItem();
        if (selectedDoc == null) {
            showAlert("Warning", "Select a document to edit.");
            return;
        }

        try {
            if (!validateFields()) return;

            selectedDoc.setDocumentType(documentTypeField.getText());
            selectedDoc.setDocumentNumber(documentNumberField.getText());
            selectedDoc.setIssueDate(Date.valueOf(issueDatePicker.getValue()));
            selectedDoc.setIssuingAuthority(issuingAuthorityField.getText());

            documentationDAO.updateDocumentation(selectedDoc);
            showAlert("Succes", "Document updated successfully.");
            refreshTable();

        } catch (SQLException e) {
            showAlert("Mistake", "The document could not be edited: " + e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        Documentation selectedDoc = documentationTable.getSelectionModel().getSelectedItem();
        if (selectedDoc == null) {
            showAlert("Warning", "Select a document to delete.");
            return;
        }

        try {
            documentationDAO.deleteDocumentationById(selectedDoc.getId());
            showAlert("Succes", "Document deleted successfully.");
            refreshTable();

        } catch (SQLException e) {
            showAlert("Mistake", "Could not delete document: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (documentTypeField.getText().isEmpty() || documentNumberField.getText().isEmpty() ||
                issueDatePicker.getValue() == null || issuingAuthorityField.getText().isEmpty()) {
            showAlert("Warning", "All fields are required.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTable() {
        try {
            List<Documentation> documentationList = documentationDAO.getAllDocumentation();
            ObservableList<Documentation> documentationObservableList = FXCollections.observableArrayList(documentationList);
            documentationTable.setItems(documentationObservableList);
        } catch (SQLException e) {
            showAlert("Mistake", "Failed to update table: " + e.getMessage());
        }
    }
}
