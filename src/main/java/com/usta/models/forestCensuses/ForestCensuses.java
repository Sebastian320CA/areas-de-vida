package com.usta.models.forestCensuses;

import java.time.LocalDate;

public class ForestCensuses {

    private int id;
    private LocalDate censusDate;
    private double vegetationCoverage;
    private String ecosystemState;
    private String observations;

    public ForestCensuses(int id, LocalDate censusDate, double vegetationCoverage, String ecosystemState, String observations) {
        this.id = id;
        this.censusDate = censusDate;
        this.vegetationCoverage = vegetationCoverage;
        this.ecosystemState = ecosystemState;
        this.observations = observations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCensusDate() {
        return censusDate;
    }

    public void setCensusDate(LocalDate censusDate) {
        this.censusDate = censusDate;
    }

    public double getVegetationCoverage() {
        return vegetationCoverage;
    }

    public void setVegetationCoverage(double vegetationCoverage) {
        this.vegetationCoverage = vegetationCoverage;
    }

    public String getEcosystemState() {
        return ecosystemState;
    }

    public void setEcosystemState(String ecosystemState) {
        this.ecosystemState = ecosystemState;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
