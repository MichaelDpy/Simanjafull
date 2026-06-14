package com.simanja.backend.model;

import jakarta.persistence.*;

/**
 * Entity User — demonstrasi Encapsulation (PBO)
 * Semua field bersifat private, diakses melalui getter & setter.
 *
 * Role: "ADMIN" atau "USER"
 */
@Entity
@Table(name = "users")
public class User {

    // =============================================
    // Fields (Encapsulation — semua private)
    // =============================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hashed

    @Column(nullable = false)
    private String role; // "ADMIN" atau "USER"

    private String username;
    private String telepon;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    @Column(name = "tanggal_lahir")
    private String tanggalLahir;

    private String alamat;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    // =============================================
    // Konstruktor
    // =============================================

    public User() {}

    public User(String nama, String email, String password, String role) {
        this.nama     = nama;
        this.email    = email;
        this.password = password;
        this.role     = role;
        this.username = email != null ? email.split("@")[0] : "";
        this.telepon      = "";
        this.jenisKelamin = "";
        this.tanggalLahir = "";
        this.alamat       = "";
        this.profileImagePath = "";
    }

    // =============================================
    // Getter & Setter (Encapsulation)
    // =============================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUsername() { return username != null ? username : ""; }
    public void setUsername(String username) { this.username = username; }

    public String getTelepon() { return telepon != null ? telepon : ""; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    public String getJenisKelamin() { return jenisKelamin != null ? jenisKelamin : ""; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getTanggalLahir() { return tanggalLahir != null ? tanggalLahir : ""; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getAlamat() { return alamat != null ? alamat : ""; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    @Override
    public String toString() {
        return "User{id=" + id + ", nama='" + nama + "', email='" + email + "', role='" + role + "'}";
    }
}
