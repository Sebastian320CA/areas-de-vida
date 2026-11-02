package com.usta.ecogrow.model;

import java.time.LocalDate;

public record LivingArea(int id, String name, String location, double hectares,
                         ConservationStatus status, LocalDate creationDate) {

    public LivingArea withId(int newId) {
        return new LivingArea(newId, name, location, hectares, status, creationDate);
    }

    /** Se usa en los ComboBox. */
    @Override
    public String toString() {
        return name;
    }
}
