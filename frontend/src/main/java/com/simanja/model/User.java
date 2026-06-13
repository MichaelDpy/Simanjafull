package com.simanja.model;

/**
 * Model User — demonstrasi Encapsulation (private fields + getter/setter)
 */
public class User {

    private int    id;
    private String nama;
    private String email;
    private String password;
    private String role;          // "ADMIN" atau "USER"
    private String username;
    private String telepon;
    private String jenisKelamin;
    private String tanggalLahir;
    private String alamat;
    private String profileImagePath; // Path lokal foto profil

    public User() {}

    public User(int id, String nama, String email, String password, String role) {
        this.id       = id;
        this.nama     = nama;
        this.email    = email;
        this.password = password;
        this.role     = role;
        // Default username dari bagian pertama email
        this.username = email != null ? email.split("@")[0] : "";
        this.telepon      = "";
        this.jenisKelamin = "";
        this.tanggalLahir = "";
        this.alamat       = "";
    }

    // Getter & Setter (Encapsulation)
    public int    getId()    { return id; }
    public void   setId(int id) { this.id = id; }

    public String getNama()  { return nama; }
    public void   setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void   setPassword(String password) { this.password = password; }

    public String getRole()  { return role; }
    public void   setRole(String role) { this.role = role; }

    public String getUsername() { return username != null ? username : ""; }
    public void   setUsername(String username) { this.username = username; }

    public String getTelepon() { return telepon != null ? telepon : ""; }
    public void   setTelepon(String telepon) { this.telepon = telepon; }

    public String getJenisKelamin() { return jenisKelamin != null ? jenisKelamin : ""; }
    public void   setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getTanggalLahir() { return tanggalLahir != null ? tanggalLahir : ""; }
    public void   setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getAlamat() { return alamat != null ? alamat : ""; }
    public void   setAlamat(String alamat) { this.alamat = alamat; }

    public String getProfileImagePath() { return profileImagePath; }
    public void   setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    @Override
    public String toString() {
        return "User{id=" + id + ", nama='" + nama + "', email='" + email + "', role='" + role + "'}";
    }
}
