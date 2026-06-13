package com.simanja.controller;

import com.simanja.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller untuk Landing Page
 */
public class LandingController {

    @FXML private Button btnMulai;
    @FXML private Button btnLogin;
    @FXML private Button btnDaftar;
    @FXML private ScrollPane mainScrollPane;
    @FXML private VBox sectionFitur;

    @FXML
    public void initialize() {
        // Tunggu layout selesai sebelum bisa hitung posisi scroll
    }

    @FXML
    private void handleScrollToTop() {
        javafx.application.Platform.runLater(() -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(mainScrollPane.vvalueProperty(), 0.0))
            );
            timeline.play();
        });
    }

    @FXML
    private void handleScrollToFitur() {
        // Jalankan setelah layout sudah terbentuk
        javafx.application.Platform.runLater(() -> {
            double contentHeight = mainScrollPane.getContent().getBoundsInLocal().getHeight();
            double nodeY = sectionFitur.getBoundsInParent().getMinY();
            double scrollValue = nodeY / (contentHeight - mainScrollPane.getViewportBounds().getHeight());

            // Clamp antara 0.0 dan 1.0
            double target = Math.min(1.0, Math.max(0.0, scrollValue));

            // Animasi smooth scroll
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(mainScrollPane.vvalueProperty(), target))
            );
            timeline.play();
        });
    }

    @FXML
    private void handleMulai() {
        SceneManager.switchTo("register");
    }

    @FXML
    private void handleLogin() {
        SceneManager.switchTo("login");
    }

    @FXML
    private void handleDaftar() {
        SceneManager.switchTo("register");
    }
}