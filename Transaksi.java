package com.simanja.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity Transaksi — demonstrasi Encapsulation (PBO) + Abstraction (PBO)
 *
 * Encapsulation : semua field bersifat private
 * Abstraction   : mengimplementasikan interface Kategorisasi
 * Polymorphism  : implementasi getKategori() dan getJenisLabel() secara polimorfis
 */
@Entity
@Table(name = "transaksi")
public class Transaksi implements Kategorisasi {

    // =============================================
    // Enum JenisTransaksi (Encapsulation inner-type)
    // =============================================
    public enum JenisTransaksi {
        PEMASUKAN,
        PENGELUARAN
    }

    // =============================================
    // Fields (Encapsulation — semua private)
    // =============================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String judul;

    @Column(nullable = false)
    private Double jumlah;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisTransaksi jenis;

    @Column(nullable = false)
    private String kategori;

    @Column(nullable = false)
    private LocalDate tanggal;

    private String keterangan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =============================================
    // Konstruktor
    // =============================================

    public Transaksi() {}

    public Transaksi(String judul, Double jumlah, JenisTransaksi jenis,
                     String kategori, LocalDate tanggal, String keterangan, User user) {
        this.judul     = judul;
        this.jumlah    = jumlah;
        this.jenis     = jenis;
        this.kategori  = kategori;
        this.tanggal   = tanggal;
        this.keterangan = keterangan;
        this.user      = user;
    }

    // =============================================
    // Implementasi interface Kategorisasi (Abstraction + Polymorphism)
    // =============================================

    @Override
    public String getKategori() { return kategori; }

    @Override
    public String getJenisLabel() {
        return jenis == JenisTransaksi.PEMASUKAN ? "Pemasukan" : "Pengeluaran";
    }

    // =============================================
    // Getter & Setter (Encapsulation)
    // =============================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public Double getJumlah() { return jumlah; }
    public void setJumlah(Double jumlah) { this.jumlah = jumlah; }

    public JenisTransaksi getJenis() { return jenis; }
    public void setJenis(JenisTransaksi jenis) { this.jenis = jenis; }

    public void setKategori(String kategori) { this.kategori = kategori; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
