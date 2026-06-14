module com.simanja {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base

    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.simanja to javafx.fxml;
    opens com.simanja.controller to javafx.fxml;
    opens com.simanja.model to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.simanja.service to com.fasterxml.jackson.databind;

    exports com.simanja;
    exports com.simanja.controller;
    exports com.simanja.model;
    exports com.simanja.service;
    exports com.simanja.util;
}
