package com.simanja.controller;

import com.simanja.service.AuthService;
import com.simanja.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller untuk Register Page
 * Demonstrasi: Validasi input
 */
public class RegisterController {

    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtKonfirmPassword;
    @FXML private Button btnDaftar;
    @FXML private Label lblError;
    @FXML private Label lblSuccess;
    @FXML private Hyperlink linkLogin;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        lblError.setVisible(false);
        lblSuccess.setVisible(false);
    }

    @FXML
    private void handleDaftar() {
        lblError.setVisible(false);
        lblSuccess.setVisible(false);

        String nama            = txtNama.getText().trim();
        String email           = txtEmail.getText().trim();
        String password        = txtPassword.getText();
        String konfirmPassword = txtKonfirmPassword.getText();

        try {
            authService.register(nama, email, password, konfirmPassword);
            lblSuccess.setText("Registrasi berhasil! Silakan login.");
            lblSuccess.setVisible(true);
            clearForm();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        SceneManager.switchTo("login");
    }

    @FXML
    private void handleKembali() {
        SceneManager.switchTo("landing");
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }

    private void clearForm() {
        txtNama.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtKonfirmPassword.clear();
    }
}
