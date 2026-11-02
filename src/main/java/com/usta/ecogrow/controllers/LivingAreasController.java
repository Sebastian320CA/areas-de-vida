package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.dao.LivingAreaDao;
import com.usta.ecogrow.model.ConservationStatus;
import com.usta.ecogrow.model.LivingArea;
import com.usta.ecogrow.ui.Badges;
import com.usta.ecogrow.ui.Form;
import com.usta.ecogrow.util.Fmt;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class LivingAreasController extends CrudController<LivingArea> {

    private final LivingAreaDao dao = new LivingAreaDao();

    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField hectaresField;
    @FXML private ComboBox<ConservationStatus> statusBox;
    @FXML private DatePicker creationDatePicker;

    @Override protected CrudDao<LivingArea> dao() { return dao; }
    @Override protected String entityName() { return "área de vida"; }
    @Override protected int idOf(LivingArea a) { return a.id(); }
    @Override protected String describe(LivingArea a) { return "Se eliminará «" + a.name() + "»."; }

    @Override
    protected String deleteWarning() {
        return "También se eliminarán sus actividades de plantación y censos forestales.";
    }

    @Override
    protected void loadExtras() {
        statusBox.getItems().setAll(ConservationStatus.values());
    }

    @Override
    protected void configureColumns() {
        column("Nombre", LivingArea::name, 200);
        column("Ubicación", LivingArea::location, 200);
        column("Hectáreas", a -> Fmt.number(a.hectares()), 110);
        TableColumn<LivingArea, ConservationStatus> status = column("Estado", LivingArea::status, 130);
        status.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(ConservationStatus s, boolean empty) {
                super.updateItem(s, empty);
                setText(null);
                setGraphic(empty || s == null ? null : Badges.of(s));
            }
        });
        column("Creación", a -> Fmt.date(a.creationDate()), 120);
    }

    @Override
    protected void fillForm(LivingArea a) {
        nameField.setText(a.name());
        locationField.setText(a.location());
        hectaresField.setText(String.valueOf(a.hectares()));
        statusBox.setValue(a.status());
        creationDatePicker.setValue(a.creationDate());
    }

    @Override
    protected void clearForm() {
        Form.reset(nameField, locationField, hectaresField, statusBox, creationDatePicker);
        statusBox.setValue(ConservationStatus.STABLE);
        creationDatePicker.setValue(LocalDate.now());
    }

    @Override
    protected LivingArea readForm() {
        String name = Form.required(nameField, "Nombre");
        String location = Form.required(locationField, "Ubicación");
        double hectares = Form.decimal(hectaresField, "Hectáreas", 0, 10_000_000);
        ConservationStatus status = Form.choice(statusBox, "Estado de conservación");
        LocalDate date = Form.date(creationDatePicker, "Fecha de creación");
        return new LivingArea(currentId(), name, location, hectares, status, date);
    }

    @Override
    protected boolean matches(LivingArea a, String q) {
        return a.name().toLowerCase().contains(q) || a.location().toLowerCase().contains(q)
                || a.status().label().toLowerCase().contains(q);
    }
}
