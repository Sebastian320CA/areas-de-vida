package com.usta.models.plantingActivities;

import javafx.beans.property.*;
import java.time.LocalDate;

public class PlantingActivities {
    private IntegerProperty id;
    private ObjectProperty<LocalDate> activityDate;
    private StringProperty activityType;
    private StringProperty description;
    private IntegerProperty livingAreaId;

    public PlantingActivities(int id, LocalDate activityDate, String activityType, String description, int livingAreaId) {
        this.id = new SimpleIntegerProperty(id);
        this.activityDate = new SimpleObjectProperty<>(activityDate);
        this.activityType = new SimpleStringProperty(activityType);
        this.description = new SimpleStringProperty(description);
        this.livingAreaId = new SimpleIntegerProperty(livingAreaId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public LocalDate getActivityDate() {
        return activityDate.get();
    }

    public ObjectProperty<LocalDate> activityDateProperty() {
        return activityDate;
    }

    public String getActivityType() {
        return activityType.get();
    }

    public StringProperty activityTypeProperty() {
        return activityType;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getLivingAreaId() {
        return livingAreaId.get();
    }

    public IntegerProperty livingAreaIdProperty() {
        return livingAreaId;
    }

    public void setActivityType(String activityType) {
        this.activityType.set(activityType);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate.set(activityDate);
    }

    public void setLivingAreaId(int livingAreaId) {
        this.livingAreaId.set(livingAreaId);
    }
}
