package com.simanja.controller;

import com.simanja.service.AuthService;
import com.simanja.model.User;
import com.simanja.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.Optional;

/**
 * Controller untuk Login Page
 * Demonstrasi: Validasi input, Security (autentikasi)
 */
public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Label lblError;
    @FXML private Hyperlink linkDaftar;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        lblError.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        lblError.setVisible(false);
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText();

        try {
            Optional<User> result = authService.login(email, password);
            if (result.isPresent()) {
                SceneManager.switchTo("dashboard");
            } else {
                showError("Email atau password salah.");
            }
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleDaftar() {
        SceneManager.switchTo("register");
    }

    @FXML
    private void handleKembali() {
        SceneManager.switchTo("landing");
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}
