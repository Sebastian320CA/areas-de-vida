package com.usta.ecogrow.controllers;

import com.usta.ecogrow.facade.DashboardFacade;
import com.usta.ecogrow.model.ConservationStatus;
import com.usta.ecogrow.model.DashboardStats;
import com.usta.ecogrow.model.PlantingActivity;
import com.usta.ecogrow.ui.Icons;
import com.usta.ecogrow.ui.Session;
import com.usta.ecogrow.ui.Toast;
import com.usta.ecogrow.util.Async;
import com.usta.ecogrow.util.Errors;
import com.usta.ecogrow.util.Fmt;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class DashboardController {

    private final DashboardFacade facade = new DashboardFacade();

    @FXML private Label greetingLabel;
    @FXML private Label dateLabel;
    @FXML private FlowPane statsPane;
    @FXML private VBox statusBox;
    @FXML private VBox recentBox;
    @FXML private BarChart<String, Number> coverageChart;

    @FXML
    private void initialize() {
        int hour = LocalTime.now().getHour();
        String greeting = hour < 12 ? "Buenos días" : hour < 19 ? "Buenas tardes" : "Buenas noches";
        String firstName = Session.get().user().name().split("\\s+")[0];
        greetingLabel.setText(greeting + ", " + firstName);
        dateLabel.setText(Fmt.date(LocalDate.now()));

        Async.run(facade::load, this::render, err -> Toast.error(Errors.message(err)));
    }

    private void render(DashboardStats s) {
        statsPane.getChildren().setAll(
                card("place", "Áreas de vida", Fmt.integer(s.areas()), Fmt.number(s.hectares()) + " hectáreas", "green"),
                card("drop", "Árboles sembrados", Fmt.integer(s.treesPlanted()), s.plantings() + " actividades", "lime"),
                card("park", "Especies registradas", Fmt.integer(s.species()), "Catálogo taxonómico", "teal"),
                card("chart", "Cobertura promedio", Fmt.percent(s.averageCoverage()), s.censuses() + " censos", "amber"),
                card("doc", "Documentos", Fmt.integer(s.documents()), "Soportes legales", "sky"));

        renderStatus(s);
        renderCoverage(s);
        renderRecent(s);
    }

    private HBox card(String icon, String title, String value, String hint, String tone) {
        Region ic = Icons.of(icon, 22);
        HBox iconBox = new HBox(ic);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setMinSize(46, 46);
        iconBox.setMaxSize(46, 46);
        iconBox.getStyleClass().addAll("stat-icon", "tone-" + tone);

        Label t = new Label(title);
        t.getStyleClass().add("stat-title");
        Label v = new Label(value);
        v.getStyleClass().add("stat-value");
        Label h = new Label(hint);
        h.getStyleClass().add("stat-hint");

        VBox text = new VBox(2, t, v, h);
        HBox card = new HBox(14, iconBox, text);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(236);
        card.getStyleClass().addAll("card", "stat-card");
        return card;
    }

    private void renderStatus(DashboardStats s) {
        statusBox.getChildren().clear();
        if (s.areas() == 0) {
            statusBox.getChildren().add(empty("Aún no hay áreas registradas."));
            return;
        }
        for (ConservationStatus st : ConservationStatus.values()) {
            int n = s.byStatus().getOrDefault(st, 0);
            Label name = new Label(st.label());
            name.setMinWidth(92);
            name.getStyleClass().add("row-label");
            ProgressBar bar = new ProgressBar((double) n / s.areas());
            bar.setMaxWidth(Double.MAX_VALUE);
            bar.getStyleClass().addAll("status-bar", "bar-" + st.css());
            HBox.setHgrow(bar, Priority.ALWAYS);
            Label count = new Label(String.valueOf(n));
            count.setMinWidth(24);
            count.setAlignment(Pos.CENTER_RIGHT);
            count.getStyleClass().add("row-value");
            HBox row = new HBox(12, name, bar, count);
            row.setAlignment(Pos.CENTER_LEFT);
            statusBox.getChildren().add(row);
        }
    }

    private void renderCoverage(DashboardStats s) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> e : s.coverageByArea().entrySet()) {
            String name = e.getKey().length() > 14 ? e.getKey().substring(0, 13) + "…" : e.getKey();
            series.getData().add(new XYChart.Data<>(name, Math.round(e.getValue() * 10) / 10.0));
        }
        coverageChart.getData().setAll(series);
    }

    private void renderRecent(DashboardStats s) {
        recentBox.getChildren().clear();
        if (s.recentActivities().isEmpty()) {
            recentBox.getChildren().add(empty("Todavía no hay actividades de plantación."));
            return;
        }
        for (PlantingActivity a : s.recentActivities()) {
            Region dot = Icons.of("leaf", 16);
            Label title = new Label(a.type() + " · " + a.areaName());
            title.getStyleClass().add("row-title");
            String detail = Fmt.date(a.date())
                    + (a.treesPlanted() > 0 ? " · " + Fmt.integer(a.treesPlanted()) + " árboles" : "")
                    + (a.treeName() != null ? " · " + a.treeName() : "");
            Label sub = new Label(detail);
            sub.getStyleClass().add("row-sub");
            VBox text = new VBox(2, title, sub);
            HBox row = new HBox(12, dot, text);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("recent-row");
            recentBox.getChildren().add(row);
        }
    }

    private Label empty(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("empty-hint");
        return l;
    }
}
