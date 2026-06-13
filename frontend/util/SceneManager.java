package com.simanja.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * SceneManager — utility untuk navigasi antar scene
 */
public class SceneManager {

    private static Stage primaryStage;
    private static final Map<String, String> FXML_MAP = new HashMap<>();

    static {
        FXML_MAP.put("landing",        "/fxml/landing.fxml");
        FXML_MAP.put("login",          "/fxml/login.fxml");
        FXML_MAP.put("register",       "/fxml/register.fxml");
        FXML_MAP.put("dashboard",      "/fxml/dashboard.fxml");
        FXML_MAP.put("transaksi",      "/fxml/transaksi.fxml");
        FXML_MAP.put("form-transaksi", "/fxml/form-transaksi.fxml");
        FXML_MAP.put("laporan",        "/fxml/laporan.fxml");
        FXML_MAP.put("target",         "/fxml/target.fxml");
        FXML_MAP.put("profil",         "/fxml/profil.fxml");
    }

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
    }

    public static void switchTo(String sceneName) {
        String fxmlPath = FXML_MAP.get(sceneName);
        if (fxmlPath == null) {
            throw new IllegalArgumentException("Scene tidak ditemukan: " + sceneName);
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Pertahankan ukuran stage yang sudah ada
            double width  = primaryStage.getWidth();
            double height = primaryStage.getHeight();

            // Fallback ke ukuran minimum jika stage belum punya ukuran
            if (Double.isNaN(width) || width < 1100)  width  = 1100;
            if (Double.isNaN(height) || height < 700) height = 700;

            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(
                SceneManager.class.getResource("/styles/dark-theme.css").toExternalForm()
            );
            primaryStage.setScene(scene);

            // Pastikan ukuran minimum tetap terjaga
            primaryStage.setMinWidth(1100);
            primaryStage.setMinHeight(700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
