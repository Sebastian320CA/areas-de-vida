package com.usta.ecogrow.ui;

import com.usta.ecogrow.util.ValidationException;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;

import java.time.LocalDate;

/** Helpers de lectura y validación de campos; marcan en rojo (.invalid) el campo con problema. */
public final class Form {

    private Form() {}

    private static void ok(Node n) {
        n.getStyleClass().remove("invalid");
    }

    private static ValidationException fail(Node n, String message) {
        if (!n.getStyleClass().contains("invalid")) {
            n.getStyleClass().add("invalid");
        }
        n.requestFocus();
        return new ValidationException(message);
    }

    public static String required(TextInputControl c, String label) {
        ok(c);
        String v = c.getText() == null ? "" : c.getText().trim();
        if (v.isEmpty()) {
            throw fail(c, "El campo «" + label + "» es obligatorio.");
        }
        return v;
    }

    public static String optional(TextInputControl c) {
        return c.getText() == null ? "" : c.getText().trim();
    }

    public static double decimal(TextInputControl c, String label, double min, double max) {
        String raw = required(c, label).replace(',', '.');
        double v;
        try {
            v = Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw fail(c, "«" + label + "» debe ser un número.");
        }
        if (v < min || v > max) {
            throw fail(c, "«" + label + "» debe estar entre " + min + " y " + max + ".");
        }
        return v;
    }

    public static int integer(TextInputControl c, String label, int min, int max) {
        String raw = required(c, label);
        int v;
        try {
            v = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw fail(c, "«" + label + "» debe ser un número entero.");
        }
        if (v < min || v > max) {
            throw fail(c, "«" + label + "» debe estar entre " + min + " y " + max + ".");
        }
        return v;
    }

    public static <V> V choice(ComboBox<V> c, String label) {
        ok(c);
        V v = c.getValue();
        if (v == null) {
            throw fail(c, "Selecciona un valor en «" + label + "».");
        }
        return v;
    }

    /** Para ComboBox editables (se puede escribir un valor propio). */
    public static String editableChoice(ComboBox<String> c, String label) {
        ok(c);
        String v = c.isEditable() ? c.getEditor().getText() : c.getValue();
        if (v == null || v.isBlank()) {
            throw fail(c, "Selecciona o escribe un valor en «" + label + "».");
        }
        return v.trim();
    }

    public static LocalDate date(DatePicker p, String label) {
        ok(p);
        LocalDate v = p.getValue();
        if (v == null) {
            throw fail(p, "Selecciona una fecha en «" + label + "».");
        }
        return v;
    }

    /** Limpia valores y marcas de error. */
    public static void reset(Node... nodes) {
        for (Node n : nodes) {
            ok(n);
            switch (n) {
                case TextInputControl t -> t.clear();
                case DatePicker d -> d.setValue(null);
                case ComboBox<?> c -> {
                    c.getSelectionModel().clearSelection();
                    c.setValue(null);
                    if (c.isEditable()) {
                        c.getEditor().clear();
                    }
                }
                default -> { }
            }
        }
    }
}
