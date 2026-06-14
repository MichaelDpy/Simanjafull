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
    @FXML private TextField txtPasswordVisible;
    @FXML private Label lblTogglePassword;
    @FXML private PasswordField txtKonfirmPassword;
    @FXML private TextField txtKonfirmPasswordVisible;
    @FXML private Label lblToggleKonfirm;
    @FXML private Button btnDaftar;
    @FXML private Label lblError;
    @FXML private Label lblSuccess;
    @FXML private Hyperlink linkLogin;

    private final AuthService authService = new AuthService();
    private boolean isPasswordVisible = false;
    private boolean isKonfirmVisible = false;

    @FXML
    public void initialize() {
        hideMessages();

        if (txtPasswordVisible != null && txtPassword != null) {
            txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());
        }
        if (txtKonfirmPasswordVisible != null && txtKonfirmPassword != null) {
            txtKonfirmPasswordVisible.textProperty().bindBidirectional(txtKonfirmPassword.textProperty());
        }

        if (lblTogglePassword != null) {
            lblTogglePassword.setOnMouseClicked(e -> togglePassword());
        }
        if (lblToggleKonfirm != null) {
            lblToggleKonfirm.setOnMouseClicked(e -> toggleKonfirm());
        }
    }

    private void togglePassword() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            lblTogglePassword.setText("🚫");
        } else {
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            lblTogglePassword.setText("👁");
        }
    }

    private void toggleKonfirm() {
        isKonfirmVisible = !isKonfirmVisible;
        if (isKonfirmVisible) {
            txtKonfirmPasswordVisible.setVisible(true);
            txtKonfirmPasswordVisible.setManaged(true);
            txtKonfirmPassword.setVisible(false);
            txtKonfirmPassword.setManaged(false);
            lblToggleKonfirm.setText("🚫");
        } else {
            txtKonfirmPasswordVisible.setVisible(false);
            txtKonfirmPasswordVisible.setManaged(false);
            txtKonfirmPassword.setVisible(true);
            txtKonfirmPassword.setManaged(true);
            lblToggleKonfirm.setText("👁");
        }
    }

    @FXML
    private void handleDaftar() {
        hideMessages();

        String nama            = txtNama.getText().trim();
        String email           = txtEmail.getText().trim();
        String password        = txtPassword.getText();
        String konfirmPassword = txtKonfirmPassword.getText();

        // Validasi client-side — tampilkan pesan error jika ada field kosong
        if (nama.isEmpty()) {
            showError("Nama lengkap tidak boleh kosong.");
            return;
        }
        if (email.isEmpty()) {
            showError("Email tidak boleh kosong.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showError("Format email tidak valid.");
            return;
        }
        if (password.isEmpty()) {
            showError("Kata sandi tidak boleh kosong.");
            return;
        }
        if (password.length() < 8) {
            showError("Kata sandi minimal 8 karakter.");
            return;
        }
        if (konfirmPassword.isEmpty()) {
            showError("Konfirmasi kata sandi tidak boleh kosong.");
            return;
        }
        if (!password.equals(konfirmPassword)) {
            showError("Konfirmasi kata sandi tidak cocok.");
            return;
        }

        try {
            authService.register(nama, email, password, konfirmPassword);
            // Registrasi berhasil dan token sudah disimpan oleh AuthService.
            // Langsung arahkan pengguna ke dashboard.
            SceneManager.switchTo("dashboard");
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
        lblError.setManaged(true);
    }

    private void showSuccess(String message) {
        lblSuccess.setText(message);
        lblSuccess.setVisible(true);
        lblSuccess.setManaged(true);
    }

    private void hideMessages() {
        lblError.setVisible(false);
        lblError.setManaged(false);
        lblSuccess.setVisible(false);
        lblSuccess.setManaged(false);
    }

    private void clearForm() {
        txtNama.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtKonfirmPassword.clear();
    }
}
