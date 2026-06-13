module com.simanja {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.simanja to javafx.fxml;
    opens com.simanja.controller to javafx.fxml;
    opens com.simanja.model to javafx.fxml;

    exports com.simanja;
    exports com.simanja.controller;
    exports com.simanja.model;
    exports com.simanja.service;
    exports com.simanja.util;
}
