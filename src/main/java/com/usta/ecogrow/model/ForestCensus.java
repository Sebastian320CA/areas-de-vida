package com.usta.ecogrow.model;

import java.time.LocalDate;

public record ForestCensus(int id, LocalDate date, double coverage, String ecosystemState,
                           String observations, int areaId, String areaName) {

    public ForestCensus withId(int newId) {
        return new ForestCensus(newId, date, coverage, ecosystemState, observations, areaId, areaName);
    }
}
