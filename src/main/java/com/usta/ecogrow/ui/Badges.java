package com.usta.ecogrow.ui;

import com.usta.ecogrow.model.ConservationStatus;
import com.usta.ecogrow.model.Role;
import javafx.scene.control.Label;

public final class Badges {

    private Badges() {}

    public static Label of(ConservationStatus status) {
        Label l = new Label(status.label());
        l.getStyleClass().addAll("badge", "badge-" + status.css());
        return l;
    }

    public static Label of(Role role) {
        Label l = new Label(role.label());
        l.getStyleClass().addAll("badge", "badge-role-" + role.name().toLowerCase());
        return l;
    }
}
