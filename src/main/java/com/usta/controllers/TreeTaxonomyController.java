package com.usta.controllers;

import java.io.IOException;

import com.usta.App;
import com.usta.models.treetaxonomy.TreeTaxonomy;
import com.usta.models.treetaxonomy.TreeTaxonomyImplDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TreeTaxonomyController {
    @FXML
    private void switchtofront() throws IOException {
        App.setRoot("FronPage");  
    }
    @FXML
    private void switchtoreturn() throws IOException {
        App.setRoot("Dashboard");  
    }

    @FXML
    private TextField speciesField;

    @FXML
    private TextField genusField;

    @FXML
    private TextField familyField;

    @FXML
    private TextField commonNameField;

    @FXML
    private TableView<TreeTaxonomy> treeTableView;

    @FXML
    private TableColumn<TreeTaxonomy, String> speciesColumn;

    @FXML
    private TableColumn<TreeTaxonomy, String> genusColumn;

    @FXML
    private TableColumn<TreeTaxonomy, String> familyColumn;

    @FXML
    private TableColumn<TreeTaxonomy, String> commonNameColumn;

    private final TreeTaxonomyImplDao dao = new TreeTaxonomyImplDao();
    private final ObservableList<TreeTaxonomy> treeData = FXCollections.observableArrayList();
    private TreeTaxonomy currentlyEditing = null;

    @FXML
public void initialize() {
    // Configurar columnas
    speciesColumn.setCellValueFactory(cellData -> cellData.getValue().speciesProperty());
    genusColumn.setCellValueFactory(cellData -> cellData.getValue().genusProperty());
    familyColumn.setCellValueFactory(cellData -> cellData.getValue().familyProperty());
    commonNameColumn.setCellValueFactory(cellData -> cellData.getValue().commonNameProperty());

    loadTreeData();

    treeTableView.setItems(treeData);

    treeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            populateFields(newSelection);
            currentlyEditing = newSelection;
        }
    });
}

private void loadTreeData() {
    treeData.clear(); 
    treeData.addAll(dao.getAllTreeTaxonomies()); 
}

    @FXML
    public void handleAddTree(@SuppressWarnings("exports") ActionEvent event) {
        String species = speciesField.getText();
        String genus = genusField.getText();
        String family = familyField.getText();
        String commonName = commonNameField.getText();

        if (species.isEmpty() || genus.isEmpty() || family.isEmpty() || commonName.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        dao.addTreeTaxonomy(species, genus, family, commonName);

        treeData.add(new TreeTaxonomy(species, genus, family, commonName));
        clearFields();
    }

    @FXML
    public void handleClearFields(@SuppressWarnings("exports") ActionEvent event) {
        clearFields();
    }

    @FXML
    public void handleEditTree(@SuppressWarnings("exports") ActionEvent event) {
        if (currentlyEditing == null) {
            showAlert("Error", "Please select a tree to edit.");
            return;
        }

        String species = speciesField.getText();
        String genus = genusField.getText();
        String family = familyField.getText();
        String commonName = commonNameField.getText();

        if (species.isEmpty() || genus.isEmpty() || family.isEmpty() || commonName.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        currentlyEditing.speciesProperty().set(species);
        currentlyEditing.genusProperty().set(genus);
        currentlyEditing.familyProperty().set(family);
        currentlyEditing.commonNameProperty().set(commonName);

        treeTableView.refresh();
        clearFields();
    }

    @FXML
    public void handleDeleteTree(@SuppressWarnings("exports") ActionEvent event) {
        TreeTaxonomy selectedTree = treeTableView.getSelectionModel().getSelectedItem();
        if (selectedTree == null) {
            showAlert("Error", "Please select a tree to delete.");
            return;
        }

        treeData.remove(selectedTree);

        clearFields();
    }

    private void populateFields(TreeTaxonomy tree) {
        speciesField.setText(tree.getSpecies());
        genusField.setText(tree.getGenus());
        familyField.setText(tree.getFamily());
        commonNameField.setText(tree.getCommonName());
    }

    private void clearFields() {
        speciesField.clear();
        genusField.clear();
        familyField.clear();
        commonNameField.clear();
        currentlyEditing = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
