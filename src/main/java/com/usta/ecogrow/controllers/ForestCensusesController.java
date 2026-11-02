package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.dao.ForestCensusDao;
import com.usta.ecogrow.dao.LivingAreaDao;
import com.usta.ecogrow.model.ForestCensus;
import com.usta.ecogrow.model.LivingArea;
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

public class ForestCensusesController extends CrudController<ForestCensus> {

    private static final List<String> STATES = List.of("Excelente", "Bueno", "Regular", "Deteriorado", "Crítico");

    private final ForestCensusDao dao = new ForestCensusDao();

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LivingArea> areaBox;
    @FXML private TextField coverageField;
    @FXML private ComboBox<String> stateBox;
    @FXML private TextArea observationsArea;

    @Override protected CrudDao<ForestCensus> dao() { return dao; }
    @Override protected String entityName() { return "censo"; }
    @Override protected int idOf(ForestCensus c) { return c.id(); }
    @Override protected String describe(ForestCensus c) {
        return "Se eliminará el censo del " + Fmt.date(c.date()) + " en " + c.areaName() + ".";
    }

    @Override
    protected void loadExtras() {
        stateBox.getItems().setAll(STATES);
        Async.run(() -> new LivingAreaDao().findAll(), areaBox.getItems()::setAll, e -> Toast.error(Errors.message(e)));
    }

    @Override
    protected void configureColumns() {
        column("Fecha", c -> Fmt.date(c.date()), 120);
        column("Área", ForestCensus::areaName, 200);
        column("Cobertura", c -> Fmt.percent(c.coverage()), 110);
        column("Estado", ForestCensus::ecosystemState, 120);
        column("Observaciones", c -> Fmt.orDash(c.observations()), 260);
    }

    @Override
    protected void fillForm(ForestCensus c) {
        datePicker.setValue(c.date());
        areaBox.getItems().stream().filter(a -> a.id() == c.areaId()).findFirst().ifPresent(areaBox::setValue);
        coverageField.setText(String.valueOf(c.coverage()));
        stateBox.setValue(c.ecosystemState());
        observationsArea.setText(c.observations());
    }

    @Override
    protected void clearForm() {
        Form.reset(datePicker, areaBox, coverageField, stateBox, observationsArea);
        datePicker.setValue(LocalDate.now());
    }

    @Override
    protected ForestCensus readForm() {
        LocalDate date = Form.date(datePicker, "Fecha del censo");
        LivingArea area = Form.choice(areaBox, "Área de vida");
        double coverage = Form.decimal(coverageField, "Cobertura vegetal (%)", 0, 100);
        String state = Form.choice(stateBox, "Estado del ecosistema");
        return new ForestCensus(currentId(), date, coverage, state, Form.optional(observationsArea),
                area.id(), area.name());
    }

    @Override
    protected boolean matches(ForestCensus c, String q) {
        return c.areaName().toLowerCase().contains(q) || c.ecosystemState().toLowerCase().contains(q)
                || (c.observations() != null && c.observations().toLowerCase().contains(q));
    }
}
