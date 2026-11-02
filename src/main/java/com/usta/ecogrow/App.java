package com.usta.ecogrow;

import com.usta.ecogrow.db.Bootstrap;
import com.usta.ecogrow.db.Database;
import com.usta.ecogrow.ui.Navigator;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

public class App extends Application {

    @Override
    public void init() {
        Locale.setDefault(Locale.of("es", "CO"));
        // Arranca en segundo plano la preparación de la base de datos mientras se dibuja la ventana.
        Bootstrap.ready();
    }

    @Override
    public void start(Stage stage) {
        Navigator nav = Navigator.get();
        nav.init(stage);
        nav.show("login");
        stage.show();
    }

    @Override
    public void stop() {
        Database.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
