package com.usta.models.livingAreas;

import java.sql.Date;
import javafx.beans.property.*;

public class LivingAreas {
    private final IntegerProperty id;
    private final StringProperty location;
    private final DoubleProperty size;
    private final StringProperty conservationStatus;
    private final ObjectProperty<Date> creationDate;

    public LivingAreas() {
        this.id = new SimpleIntegerProperty();
        this.location = new SimpleStringProperty();
        this.size = new SimpleDoubleProperty();
        this.conservationStatus = new SimpleStringProperty();
        this.creationDate = new SimpleObjectProperty<>();
    }

    public LivingAreas(int id, String location, double size, String conservationStatus, Date creationDate) {
        this();
        this.id.set(id);
        this.location.set(location);
        this.size.set(size);
        this.conservationStatus.set(conservationStatus);
        this.creationDate.set(creationDate);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

    public StringProperty conservationStatusProperty() {
        return conservationStatus;
    }

    public ObjectProperty<Date> creationDateProperty() {
        return creationDate;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public double getSize() {
        return size.get();
    }

    public void setSize(double size) {
        this.size.set(size);
    }

    public String getConservationStatus() {
        return conservationStatus.get();
    }

    public void setConservationStatus(String conservationStatus) {
        this.conservationStatus.set(conservationStatus);
    }

    public Date getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate.set(creationDate);
    }

    @Override
    public String toString() {
        return location.get(); 
    }
}
