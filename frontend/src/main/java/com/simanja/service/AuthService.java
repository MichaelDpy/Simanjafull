package com.simanja.service;

import com.simanja.model.User;
import com.simanja.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * AuthService — Service layer untuk autentikasi
 * Demonstrasi: Service layer, Encapsulation, Validasi
 */
public class AuthService {

    // Data dummy pengguna
    private static final List<User> dummyUsers = new ArrayList<>();

    static {
        User admin = new User(1, "Admin SiManja", "admin@simanja.com", "admin123", "ADMIN");
        admin.setUsername("adminsimanja");
        admin.setTelepon("0812-0000-0001");
        admin.setJenisKelamin("Laki-laki");
        admin.setTanggalLahir("01 Januari 2000");
        admin.setAlamat("Jl. Simanja No. 1, Jakarta Pusat");
        dummyUsers.add(admin);

        User budi = new User(2, "Budi Santoso", "budi@simanja.com", "budi123", "USER");
        budi.setUsername("budisantoso");
        budi.setTelepon("0812-3456-7890");
        budi.setJenisKelamin("Laki-laki");
        budi.setTanggalLahir("15 Juni 1995");
        budi.setAlamat("Jl. Midnight Ledger No. 88, Jakarta Selatan");
        dummyUsers.add(budi);

        User siti = new User(3, "Siti Rahayu", "siti@simanja.com", "siti123", "USER");
        siti.setUsername("sitirahayu");
        siti.setTelepon("0856-7890-1234");
        siti.setJenisKelamin("Perempuan");
        siti.setTanggalLahir("20 Maret 1998");
        siti.setAlamat("Jl. Mawar No. 12, Bandung");
        dummyUsers.add(siti);
    }

    /**
     * Login dengan validasi email dan password
     * @return User jika berhasil, kosong jika gagal
     */
    public Optional<User> login(String email, String password) {
        // Validasi input
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email tidak boleh kosong.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Format email tidak valid.");
        }

        Optional<User> user = dummyUsers.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email)
                      && u.getPassword().equals(password))
            .findFirst();

        user.ifPresent(u -> SessionManager.getInstance().login(u));
        return user;
    }

    /**
     * Register pengguna baru
     */
    public User register(String nama, String email, String password, String konfirmPassword) {
        // Validasi
        if (nama == null || nama.isBlank())
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Format email tidak valid.");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        if (!password.equals(konfirmPassword))
            throw new IllegalArgumentException("Konfirmasi password tidak cocok.");

        boolean emailExists = dummyUsers.stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExists)
            throw new IllegalArgumentException("Email sudah terdaftar.");

        User newUser = new User(dummyUsers.size() + 1, nama, email, password, "USER");
        dummyUsers.add(newUser);
        return newUser;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }
}
