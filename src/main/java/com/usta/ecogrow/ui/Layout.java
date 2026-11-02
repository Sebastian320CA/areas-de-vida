package com.usta.ecogrow.ui;

import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public final class Layout {

    private Layout() {}

    /** Recorta el contenido al tamaño del contenedor (para que los círculos decorativos no se salgan). */
    public static void clipToBounds(Region region) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(region.widthProperty());
        clip.heightProperty().bind(region.heightProperty());
        region.setClip(clip);
    }
}
