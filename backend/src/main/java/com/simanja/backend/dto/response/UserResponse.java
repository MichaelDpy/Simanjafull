package com.simanja.backend.dto.response;

import com.simanja.backend.model.User;

/**
 * DTO response data user (tanpa password).
 */
public class UserResponse {

    private Long id;
    private String nama;
    private String email;
    private String role;
    private String username;
    private String telepon;
    private String jenisKelamin;
    private String tanggalLahir;
    private String alamat;
    private String profileImagePath;

    public UserResponse() {}

    /**
     * Factory method untuk konversi dari entity User ke UserResponse.
     * Password tidak disertakan.
     */
    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.id           = user.getId();
        dto.nama         = user.getNama();
        dto.email        = user.getEmail();
        dto.role         = user.getRole();
        dto.username     = user.getUsername();
        dto.telepon      = user.getTelepon();
        dto.jenisKelamin = user.getJenisKelamin();
        dto.tanggalLahir = user.getTanggalLahir();
        dto.alamat       = user.getAlamat();
        dto.profileImagePath = user.getProfileImagePath();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
}
