package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.dao.TreeSpeciesDao;
import com.usta.ecogrow.model.TreeSpecies;
import com.usta.ecogrow.ui.Form;
import com.usta.ecogrow.util.Fmt;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TreeSpeciesController extends CrudController<TreeSpecies> {

    private final TreeSpeciesDao dao = new TreeSpeciesDao();

    @FXML private TextField commonNameField;
    @FXML private TextField speciesField;
    @FXML private TextField genusField;
    @FXML private TextField familyField;

    @Override protected CrudDao<TreeSpecies> dao() { return dao; }
    @Override protected String entityName() { return "especie"; }
    @Override protected int idOf(TreeSpecies t) { return t.id(); }
    @Override protected String describe(TreeSpecies t) { return "Se eliminará «" + t + "»."; }

    @Override
    protected String deleteWarning() {
        return "Las actividades de plantación que la usan quedarán sin especie asignada.";
    }

    @Override
    protected void configureColumns() {
        column("Nombre común", t -> Fmt.orDash(t.commonName()), 180);
        column("Especie", TreeSpecies::species, 200);
        column("Género", t -> Fmt.orDash(t.genus()), 140);
        column("Familia", t -> Fmt.orDash(t.family()), 140);
    }

    @Override
    protected void fillForm(TreeSpecies t) {
        commonNameField.setText(t.commonName());
        speciesField.setText(t.species());
        genusField.setText(t.genus());
        familyField.setText(t.family());
    }

    @Override
    protected void clearForm() {
        Form.reset(commonNameField, speciesField, genusField, familyField);
    }

    @Override
    protected TreeSpecies readForm() {
        return new TreeSpecies(currentId(),
                Form.required(speciesField, "Especie (nombre científico)"),
                Form.optional(genusField),
                Form.optional(familyField),
                Form.required(commonNameField, "Nombre común"));
    }

    @Override
    protected boolean matches(TreeSpecies t, String q) {
        return t.species().toLowerCase().contains(q) || t.commonName().toLowerCase().contains(q)
                || t.genus().toLowerCase().contains(q) || t.family().toLowerCase().contains(q);
    }
}
