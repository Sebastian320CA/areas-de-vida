package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.dao.LivingAreaDao;
import com.usta.ecogrow.dao.PlantingActivityDao;
import com.usta.ecogrow.dao.TreeSpeciesDao;
import com.usta.ecogrow.model.LivingArea;
import com.usta.ecogrow.model.PlantingActivity;
import com.usta.ecogrow.model.TreeSpecies;
import com.usta.ecogrow.ui.Form;
import com.usta.ecogrow.ui.Toast;
import com.usta.ecogrow.util.Async;
import com.usta.ecogrow.util.Errors;
import com.usta.ecogrow.util.Fmt;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public class PlantingActivitiesController extends CrudController<PlantingActivity> {

    private static final List<String> TYPES =
            List.of("Siembra", "Replante", "Mantenimiento", "Riego", "Poda", "Monitoreo");

    private final PlantingActivityDao dao = new PlantingActivityDao();

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> typeBox;
    @FXML private ComboBox<LivingArea> areaBox;
    @FXML private ComboBox<TreeSpecies> treeBox;
    @FXML private TextField treesField;
    @FXML private TextArea descriptionArea;

    @Override protected CrudDao<PlantingActivity> dao() { return dao; }
    @Override protected String entityName() { return "actividad"; }
    @Override protected int idOf(PlantingActivity p) { return p.id(); }
    @Override protected String describe(PlantingActivity p) {
        return "Se eliminará la actividad «" + p.type() + "» del " + Fmt.date(p.date()) + " en " + p.areaName() + ".";
    }

    @Override
    protected void loadExtras() {
        typeBox.getItems().setAll(TYPES);
        Async.run(() -> new LivingAreaDao().findAll(), areaBox.getItems()::setAll, e -> Toast.error(Errors.message(e)));
        Async.run(() -> new TreeSpeciesDao().findAll(), treeBox.getItems()::setAll, e -> Toast.error(Errors.message(e)));
    }

    @Override
    protected void configureColumns() {
        column("Fecha", p -> Fmt.date(p.date()), 120);
        column("Tipo", PlantingActivity::type, 120);
        column("Área", PlantingActivity::areaName, 190);
        column("Especie", p -> Fmt.orDash(p.treeName()), 160);
        column("Árboles", p -> Fmt.integer(p.treesPlanted()), 90);
    }

    @Override
    protected void fillForm(PlantingActivity p) {
        datePicker.setValue(p.date());
        typeBox.setValue(p.type());
        areaBox.getItems().stream().filter(a -> a.id() == p.areaId()).findFirst().ifPresent(areaBox::setValue);
        if (p.treeId() == null) {
            treeBox.setValue(null);
        } else {
            treeBox.getItems().stream().filter(t -> t.id() == p.treeId()).findFirst().ifPresent(treeBox::setValue);
        }
        treesField.setText(String.valueOf(p.treesPlanted()));
        descriptionArea.setText(p.description());
    }

    @Override
    protected void clearForm() {
        Form.reset(datePicker, typeBox, areaBox, treeBox, treesField, descriptionArea);
        datePicker.setValue(LocalDate.now());
        typeBox.setValue(TYPES.getFirst());
        treesField.setText("0");
    }

    @Override
    protected PlantingActivity readForm() {
        LocalDate date = Form.date(datePicker, "Fecha");
        String type = Form.choice(typeBox, "Tipo de actividad");
        LivingArea area = Form.choice(areaBox, "Área de vida");
        int trees = Form.integer(treesField, "Árboles plantados", 0, 1_000_000);
        TreeSpecies tree = treeBox.getValue();
        return new PlantingActivity(currentId(), date, type, Form.optional(descriptionArea), trees,
                area.id(), area.name(), tree == null ? null : tree.id(), tree == null ? null : tree.toString());
    }

    @Override
    protected boolean matches(PlantingActivity p, String q) {
        return p.type().toLowerCase().contains(q) || p.areaName().toLowerCase().contains(q)
                || (p.treeName() != null && p.treeName().toLowerCase().contains(q))
                || (p.description() != null && p.description().toLowerCase().contains(q));
    }
}
