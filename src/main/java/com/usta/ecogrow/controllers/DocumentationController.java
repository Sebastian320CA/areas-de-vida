package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.dao.DocumentationDao;
import com.usta.ecogrow.dao.LivingAreaDao;
import com.usta.ecogrow.model.Documentation;
import com.usta.ecogrow.model.LivingArea;
import com.usta.ecogrow.ui.Form;
import com.usta.ecogrow.ui.Toast;
import com.usta.ecogrow.util.Async;
import com.usta.ecogrow.util.Errors;
import com.usta.ecogrow.util.Fmt;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public class DocumentationController extends CrudController<Documentation> {

    private static final List<String> TYPES = List.of("Resolución de declaratoria", "Licencia ambiental",
            "Concepto técnico", "Plan de manejo", "Escritura", "Permiso de aprovechamiento", "Otro");

    private final DocumentationDao dao = new DocumentationDao();

    @FXML private ComboBox<String> typeBox;
    @FXML private TextField numberField;
    @FXML private DatePicker issueDatePicker;
    @FXML private TextField authorityField;
    @FXML private ComboBox<LivingArea> areaBox;

    @Override protected CrudDao<Documentation> dao() { return dao; }
    @Override protected String entityName() { return "documento"; }
    @Override protected int idOf(Documentation d) { return d.id(); }
    @Override protected String describe(Documentation d) { return "Se eliminará el documento «" + d.number() + "»."; }

    @Override
    protected void loadExtras() {
        typeBox.getItems().setAll(TYPES);
        Async.run(() -> new LivingAreaDao().findAll(), areaBox.getItems()::setAll, e -> Toast.error(Errors.message(e)));
    }

    @Override
    protected void configureColumns() {
        column("Tipo", Documentation::type, 190);
        column("Número", Documentation::number, 130);
        column("Emisión", d -> Fmt.date(d.issueDate()), 120);
        column("Autoridad", d -> Fmt.orDash(d.authority()), 170);
        column("Área", d -> Fmt.orDash(d.areaName()), 170);
    }

    @Override
    protected void fillForm(Documentation d) {
        typeBox.setValue(d.type());
        typeBox.getEditor().setText(d.type());
        numberField.setText(d.number());
        issueDatePicker.setValue(d.issueDate());
        authorityField.setText(d.authority());
        if (d.areaId() == null) {
            areaBox.setValue(null);
        } else {
            areaBox.getItems().stream().filter(a -> a.id() == d.areaId()).findFirst().ifPresent(areaBox::setValue);
        }
    }

    @Override
    protected void clearForm() {
        Form.reset(typeBox, numberField, issueDatePicker, authorityField, areaBox);
        issueDatePicker.setValue(LocalDate.now());
    }

    @Override
    protected Documentation readForm() {
        String type = Form.editableChoice(typeBox, "Tipo de documento");
        String number = Form.required(numberField, "Número");
        LocalDate date = Form.date(issueDatePicker, "Fecha de emisión");
        LivingArea area = areaBox.getValue();
        return new Documentation(currentId(), type, number, date, Form.optional(authorityField),
                area == null ? null : area.id(), area == null ? null : area.name());
    }

    @Override
    protected boolean matches(Documentation d, String q) {
        return d.type().toLowerCase().contains(q) || d.number().toLowerCase().contains(q)
                || (d.authority() != null && d.authority().toLowerCase().contains(q))
                || (d.areaName() != null && d.areaName().toLowerCase().contains(q));
    }
}
