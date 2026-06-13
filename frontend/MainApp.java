package com.simanja;

import com.simanja.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.init(primaryStage);
        primaryStage.setWidth(1100);
        primaryStage.setHeight(700);
        SceneManager.switchTo("landing");
        primaryStage.setTitle("SiManja - Sistem Manajemen Keuangan");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
