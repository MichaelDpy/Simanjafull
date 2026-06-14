package com.simanja.backend.service;

import com.simanja.backend.dto.request.ChangePasswordRequest;
import com.simanja.backend.dto.request.UpdateUserRequest;
import com.simanja.backend.dto.response.UserResponse;
import com.simanja.backend.exception.ResourceNotFoundException;
import com.simanja.backend.exception.ValidationException;
import com.simanja.backend.model.User;
import com.simanja.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService — Service layer untuk logika bisnis terkait User.
 * Demonstrasi: Service layer, Encapsulation, Validasi.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Ambil data user berdasarkan email.
     */
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan email " + email + " tidak ditemukan"));
        return UserResponse.from(user);
    }

    /**
     * Ambil data user berdasarkan ID.
     */
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return UserResponse.from(user);
    }

    /**
     * Ambil semua user — khusus ADMIN.
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Update profil user berdasarkan email (dari JWT token).
     */
    public UserResponse updateProfil(String email, UpdateUserRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan email " + email + " tidak ditemukan"));

        // Update field yang tidak null / tidak blank
        if (request.getNama() != null && !request.getNama().isBlank()) {
            user.setNama(request.getNama());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getTelepon() != null) {
            user.setTelepon(request.getTelepon());
        }
        if (request.getJenisKelamin() != null) {
            user.setJenisKelamin(request.getJenisKelamin());
        }
        if (request.getTanggalLahir() != null) {
            user.setTanggalLahir(request.getTanggalLahir());
        }
        if (request.getAlamat() != null) {
            user.setAlamat(request.getAlamat());
        }
        if (request.getProfileImagePath() != null) {
            user.setProfileImagePath(request.getProfileImagePath());
        }

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    /**
     * Ambil entity User internal berdasarkan email (digunakan oleh service lain).
     */
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan email " + email + " tidak ditemukan"));
    }

    /**
     * Ubah password user.
     * Validasi: password lama harus cocok, password baru harus cocok dengan konfirmasi.
     */
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan email " + email + " tidak ditemukan"));

        // Validasi bisnis — password lama harus cocok
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ValidationException("Password lama tidak cocok");
        }

        // Validasi bisnis — password baru harus cocok dengan konfirmasi
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Konfirmasi password baru tidak cocok");
        }

        // Validasi bisnis — password baru tidak boleh sama dengan password lama
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new ValidationException("Password baru harus berbeda dengan password lama");
        }

        // Update password dengan hash baru
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
