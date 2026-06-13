package com.simanja.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Utility untuk toggle sidebar (hamburger menu) dengan animasi slide.
 */
public class SidebarToggle {

    private static final double SIDEBAR_OPEN_WIDTH  = 220;
    private static final double SIDEBAR_CLOSE_WIDTH = 0;
    private static final int    ANIM_MS             = 200;

    private boolean sidebarOpen = true;

    /**
     * Toggle buka/tutup sidebar dengan animasi.
     *
     * @param sidebar VBox sidebar yang akan di-toggle
     */
    public void toggle(VBox sidebar) {
        double targetWidth = sidebarOpen ? SIDEBAR_CLOSE_WIDTH : SIDEBAR_OPEN_WIDTH;

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(ANIM_MS),
                new KeyValue(sidebar.prefWidthProperty(), targetWidth),
                new KeyValue(sidebar.minWidthProperty(), targetWidth),
                new KeyValue(sidebar.maxWidthProperty(), targetWidth)
            )
        );
        timeline.play();

        // Sembunyikan konten saat menutup, tampilkan saat membuka
        if (sidebarOpen) {
            timeline.setOnFinished(e -> sidebar.setVisible(false));
        } else {
            sidebar.setVisible(true);
        }

        sidebarOpen = !sidebarOpen;
    }

    public boolean isSidebarOpen() {
        return sidebarOpen;
    }
}
