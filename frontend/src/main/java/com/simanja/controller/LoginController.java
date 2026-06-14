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
    @FXML private TextField txtPasswordVisible;
    @FXML private Label lblTogglePassword;
    @FXML private Button btnLogin;
    @FXML private Label lblError;
    @FXML private Hyperlink linkDaftar;

    private final AuthService authService = new AuthService();
    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        lblError.setVisible(false);
        lblError.setManaged(false);

        // Binding dua arah agar ketikan di PasswordField dan TextField tetap sinkron
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());

        // Event handler untuk toggle
        if (lblTogglePassword != null) {
            lblTogglePassword.setOnMouseClicked(e -> togglePasswordVisibility());
        }
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            // Tampilkan TextField, sembunyikan PasswordField
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            lblTogglePassword.setText("🚫"); // icon tutup mata / silang
        } else {
            // Tampilkan PasswordField, sembunyikan TextField
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            lblTogglePassword.setText("👁"); // icon buka mata
        }
    }

    @FXML
    private void handleLogin() {
        lblError.setVisible(false);
        lblError.setManaged(false);
        
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText();

        if (email.isEmpty()) {
            showError("Email tidak boleh kosong.");
            return;
        }
        if (password.isEmpty()) {
            showError("Kata sandi tidak boleh kosong.");
            return;
        }

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
        lblError.setManaged(true);
    }
}
