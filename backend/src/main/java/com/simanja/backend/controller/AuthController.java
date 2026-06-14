package com.simanja.backend.controller;

import com.simanja.backend.dto.request.LoginRequest;
import com.simanja.backend.dto.request.RegisterRequest;
import com.simanja.backend.dto.response.AuthResponse;
import com.simanja.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — REST Controller untuk autentikasi.
 * Endpoint publik: tidak memerlukan JWT token.
 *
 * Demonstrasi: Controller layer (MVC), Security
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * Login dengan email + password, kembalikan JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/register
     * Daftar akun baru, langsung kembalikan JWT token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
