package com.simanja.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simanja.model.User;
import com.simanja.util.ApiClient;
import com.simanja.util.SessionManager;

import java.util.Optional;

/**
 * AuthService — Service layer untuk autentikasi
 * Terintegrasi dengan backend REST API (Spring Boot)
 */
public class AuthService {

    /**
     * DTO records untuk request & response ke backend.
     * Menggunakan Java 17 records agar lebih ringkas.
     */
    record LoginRequest(String email, String password) {}
    
    record RegisterRequest(String nama, String email, String password, String konfirmPassword) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record AuthResponse(String token, int userId, String nama, String email, String role) {}

    public Optional<User> login(String email, String password) {
        // Validasi input sisi klien (opsional, untuk UX yang lebih cepat)
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email tidak boleh kosong.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        }

        try {
            LoginRequest req = new LoginRequest(email, password);
            AuthResponse resp = ApiClient.post("/auth/login", req, AuthResponse.class);

            // Mapping dari DTO ke Model frontend (hanya info dasar)
            User tempUser = new User(resp.userId(), resp.nama(), resp.email(), "", resp.role());
            
            // Simpan state user & JWT Token sementara untuk mengambil data profil yang butuh Auth Header
            SessionManager.getInstance().login(tempUser, resp.token());
            
            // Ambil profile lengkap dari backend yang memuat profileImagePath, alamat, dll
            try {
                UserService userService = new UserService();
                User fullProfile = userService.getProfile();
                // Override email dari AuthResponse jika getProfile() tidak memberikan email (atau kosong)
                if (fullProfile.getEmail() == null || fullProfile.getEmail().isEmpty()) {
                    fullProfile.setEmail(resp.email());
                }
                SessionManager.getInstance().login(fullProfile, resp.token());
                return Optional.of(fullProfile);
            } catch (Exception ex) {
                // Jika getProfile gagal, gunakan tempUser sebagai fallback
                System.err.println("Gagal mengambil data profil penuh saat login: " + ex.getMessage());
                return Optional.of(tempUser);
            }
        } catch (RuntimeException e) {
            System.err.println("Login gagal: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Register pengguna baru via REST API
     */
    public User register(String nama, String email, String password, String konfirmPassword) {
        try {
            RegisterRequest req = new RegisterRequest(nama, email, password, konfirmPassword);
            AuthResponse resp = ApiClient.post("/auth/register", req, AuthResponse.class);

            User tempUser = new User(resp.userId(), resp.nama(), resp.email(), "", resp.role());
            
            // Simpan state & token sementara
            SessionManager.getInstance().login(tempUser, resp.token());
            
            // Ambil profile lengkap untuk menjamin format model frontend konsisten (walau akun baru)
            try {
                UserService userService = new UserService();
                User fullProfile = userService.getProfile();
                if (fullProfile.getEmail() == null || fullProfile.getEmail().isEmpty()) {
                    fullProfile.setEmail(resp.email());
                }
                SessionManager.getInstance().login(fullProfile, resp.token());
                return fullProfile;
            } catch (Exception ex) {
                System.err.println("Gagal mengambil data profil penuh saat register: " + ex.getMessage());
                return tempUser;
            }
        } catch (RuntimeException e) {
            System.err.println("Register gagal: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }
}
