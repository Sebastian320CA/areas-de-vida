file://<WORKSPACE>/src/main/java/module-info.java
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 144
uri: file://<WORKSPACE>/src/main/java/module-info.java
text:
```scala
module com.usta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java@@fx.base;

    opens com.usta.controllers to javafx.fxml; 
    opens com.usta.models.plantingActivities to javafx.base;
    opens com.usta.models.forestCensuses to javafx.base; 
    opens com.usta.models.livingAreas to javafx.base;
    opens com.usta.models.documentations to javafx.base;


    exports com.usta; 
    exports com.usta.controllers; 
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 