package com.usta.ecogrow.model;

import java.time.LocalDate;

public record Documentation(int id, String type, String number, LocalDate issueDate,
                            String authority, Integer areaId, String areaName) {

    public Documentation withId(int newId) {
        return new Documentation(newId, type, number, issueDate, authority, areaId, areaName);
    }
}
