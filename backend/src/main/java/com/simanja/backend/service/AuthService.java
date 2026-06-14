package com.simanja.backend.service;

import com.simanja.backend.dto.request.LoginRequest;
import com.simanja.backend.dto.request.RegisterRequest;
import com.simanja.backend.dto.response.AuthResponse;
import com.simanja.backend.exception.ValidationException;
import com.simanja.backend.model.User;
import com.simanja.backend.repository.UserRepository;
import com.simanja.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService — Service layer untuk autentikasi dan registrasi.
 *
 * Demonstrasi:
 * - Service Layer (MVC)
 * - Security (autentikasi, JWT, hashing password)
 * - Validasi data (input + bisnis)
 * - Encapsulation: logika auth tersembunyi di balik interface service
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository       = userRepository;
        this.passwordEncoder      = passwordEncoder;
        this.jwtUtil              = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Login pengguna.
     * Validasi credential menggunakan Spring Security AuthenticationManager.
     * Jika berhasil, kembalikan JWT token.
     */
    public AuthResponse login(LoginRequest request) {
        // Spring Security melakukan validasi email + password (termasuk BCrypt check)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Ambil data user untuk response
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("User tidak ditemukan"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getId(), user.getNama(), user.getEmail(), user.getRole());
    }

    /**
     * Registrasi pengguna baru.
     * Validasi: password cocok, email belum terdaftar.
     */
    public AuthResponse register(RegisterRequest request) {
        // Validasi bisnis — konfirmasi password
        if (!request.getPassword().equals(request.getKonfirmPassword())) {
            throw new ValidationException("Konfirmasi password tidak cocok");
        }

        // Validasi bisnis — email belum terdaftar
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email '" + request.getEmail() + "' sudah terdaftar");
        }

        // Buat user baru dengan password di-hash BCrypt
        User newUser = new User(
                request.getNama(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "USER"
        );

        User saved = userRepository.save(newUser);

        // Generate JWT token langsung setelah register
        String token = jwtUtil.generateToken(saved.getEmail());

        return new AuthResponse(token, saved.getId(), saved.getNama(), saved.getEmail(), saved.getRole());
    }
}
