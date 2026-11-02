package com.usta.ecogrow.model;

import java.time.LocalDate;

/** areaName y treeName son datos de solo lectura traídos con JOIN. */
public record PlantingActivity(int id, LocalDate date, String type, String description, int treesPlanted,
                               int areaId, String areaName, Integer treeId, String treeName) {

    public PlantingActivity withId(int newId) {
        return new PlantingActivity(newId, date, type, description, treesPlanted, areaId, areaName, treeId, treeName);
    }
}
