package com.usta.ecogrow.model;

public enum ConservationStatus {
    CRITICAL("Crítico", "critical"),
    ENDANGERED("En peligro", "endangered"),
    AT_RISK("En riesgo", "at-risk"),
    VULNERABLE("Vulnerable", "vulnerable"),
    STABLE("Estable", "stable"),
    IMPROVING("En mejora", "improving");

    private final String label;
    private final String css;

    ConservationStatus(String label, String css) {
        this.label = label;
        this.css = css;
    }

    public String label() {
        return label;
    }

    /** Sufijo de la clase CSS (badge-xxx / bar-xxx). */
    public String css() {
        return css;
    }

    @Override
    public String toString() {
        return label;
    }

    public static ConservationStatus fromDb(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return STABLE;
        }
    }
}
