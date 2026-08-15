package com.usta.models.treetaxonomy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TreeTaxonomy {
    private final StringProperty species;
    private final StringProperty genus;
    private final StringProperty family;
    private final StringProperty commonName;

    public TreeTaxonomy(String species, String genus, String family, String commonName) {
        this.species = new SimpleStringProperty(species);
        this.genus = new SimpleStringProperty(genus);
        this.family = new SimpleStringProperty(family);
        this.commonName = new SimpleStringProperty(commonName);
    }

    public String getSpecies() {
        return species.get();
    }

    public StringProperty speciesProperty() {
        return species;
    }

    public String getGenus() {
        return genus.get();
    }

    public StringProperty genusProperty() {
        return genus;
    }

    public String getFamily() {
        return family.get();
    }

    public StringProperty familyProperty() {
        return family;
    }

    public String getCommonName() {
        return commonName.get();
    }

    public StringProperty commonNameProperty() {
        return commonName;
    }
}
