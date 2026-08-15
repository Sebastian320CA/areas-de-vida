module com.usta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;

    opens com.usta.controllers to javafx.fxml; 
    opens com.usta.models.plantingActivities to javafx.base;
    opens com.usta.models.forestCensuses to javafx.base; 
    opens com.usta.models.livingAreas to javafx.base;
    opens com.usta.models.documentations to javafx.base;


    exports com.usta; 
    exports com.usta.controllers; 
}
