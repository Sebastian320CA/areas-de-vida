package com.usta.ecogrow.controllers;

import com.usta.ecogrow.dao.CrudDao;
import com.usta.ecogrow.ui.Dialogs;
import com.usta.ecogrow.ui.Session;
import com.usta.ecogrow.ui.Toast;
import com.usta.ecogrow.util.Async;
import com.usta.ecogrow.util.Errors;
import com.usta.ecogrow.util.ValidationException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.function.Function;

/**
 * Base de todas las pantallas de gestión: tabla con búsqueda y orden + formulario lateral.
 * Cada pantalla solo define columnas, campos y cómo leer/pintar el formulario.
 * Todo el acceso a base de datos corre fuera del hilo de JavaFX.
 */
public abstract class CrudController<T> {

    @FXML protected TableView<T> table;
    @FXML protected TextField searchField;
    @FXML protected Button newButton;
    @FXML protected Button saveButton;
    @FXML protected Button deleteButton;
    @FXML protected Label formTitle;
    @FXML protected Label countLabel;
    @FXML protected Node formPane;

    protected final ObservableList<T> items = FXCollections.observableArrayList();
    private FilteredList<T> filtered;
    protected T selected;

    // ---- a implementar por cada pantalla ----
    protected abstract CrudDao<T> dao();
    protected abstract String entityName();          // "área de vida"
    protected abstract void configureColumns();
    protected abstract void fillForm(T item);
    protected abstract void clearForm();
    /** Lee y valida el formulario; lanzar ValidationException si hay datos inválidos. */
    protected abstract T readForm();
    protected abstract boolean matches(T item, String lowerCaseQuery);
    protected abstract int idOf(T item);
    protected abstract String describe(T item);       // texto para el diálogo de eliminación

    // ---- ganchos opcionales ----
    protected void loadExtras() { }
    protected T persist(T item) throws Exception { return dao().save(item); }
    protected void remove(T item) throws Exception { dao().delete(idOf(item)); }
    protected String deleteWarning() { return "Esta acción no se puede deshacer."; }

    protected final int currentId() {
        return selected == null ? 0 : idOf(selected);
    }

    @FXML
    public void initialize() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label("Aún no hay registros. Crea el primero desde el formulario."));
        configureColumns();

        filtered = new FilteredList<>(items, t -> true);
        SortedList<T> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        searchField.textProperty().addListener((obs, old, query) -> {
            String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
            filtered.setPredicate(t -> q.isEmpty() || matches(t, q));
            updateCount();
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, item) -> {
            if (item != null && Session.get().canWrite()) {
                selected = item;
                fillForm(item);
                updateFormState();
            }
        });

        boolean canWrite = Session.get().canWrite();
        setShown(formPane, canWrite);
        setShown(newButton, canWrite);
        if (canWrite) {
            newButton.setOnAction(e -> startNew());
            saveButton.setOnAction(e -> save());
            deleteButton.setOnAction(e -> delete());
            startNew();
        }
        loadExtras();
        reload();
    }

    private static void setShown(Node node, boolean shown) {
        node.setVisible(shown);
        node.setManaged(shown);
    }

    protected final <V> TableColumn<T, V> column(String title, Function<T, V> getter, double width) {
        TableColumn<T, V> col = new TableColumn<>(title);
        col.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(getter.apply(c.getValue())));
        col.setPrefWidth(width);
        table.getColumns().add(col);
        return col;
    }

    protected final void startNew() {
        selected = null;
        table.getSelectionModel().clearSelection();
        clearForm();
        updateFormState();
    }

    private void updateFormState() {
        formTitle.setText(selected == null ? "Nuevo registro" : "Editar registro");
        saveButton.setText(selected == null ? "Crear" : "Guardar cambios");
        deleteButton.setDisable(selected == null);
    }

    protected final void reload() {
        Async.run(() -> dao().findAll(), list -> {
            items.setAll(list);
            updateCount();
        }, err -> Toast.error(Errors.message(err)));
    }

    private void updateCount() {
        countLabel.setText(filtered.size() + " de " + items.size() + " registros");
    }

    private void save() {
        T item;
        try {
            item = readForm();
        } catch (ValidationException e) {
            Toast.error(e.getMessage());
            return;
        }
        boolean creating = selected == null;
        saveButton.setDisable(true);
        Async.run(() -> persist(item), saved -> {
            saveButton.setDisable(false);
            Toast.success(creating ? "Registro creado correctamente." : "Cambios guardados.");
            reload();
            startNew();
        }, err -> {
            saveButton.setDisable(false);
            Toast.error(Errors.message(err));
        });
    }

    private void delete() {
        if (selected == null) {
            return;
        }
        T target = selected;
        boolean ok = Dialogs.confirm("¿Eliminar " + entityName() + "?",
                describe(target) + "\n\n" + deleteWarning(), "Eliminar");
        if (!ok) {
            return;
        }
        Async.exec(() -> remove(target), () -> {
            Toast.success("Registro eliminado.");
            reload();
            startNew();
        }, err -> Toast.error(Errors.message(err)));
    }
}
