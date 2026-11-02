package com.usta.ecogrow.model;

public record TreeSpecies(int id, String species, String genus, String family, String commonName) {

    public TreeSpecies withId(int newId) {
        return new TreeSpecies(newId, species, genus, family, commonName);
    }

    @Override
    public String toString() {
        return commonName == null || commonName.isBlank() ? species : commonName + " (" + species + ")";
    }
}
