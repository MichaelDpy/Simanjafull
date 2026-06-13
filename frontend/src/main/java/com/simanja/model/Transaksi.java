package com.simanja.model;

import java.time.LocalDate;

/**
 * Model Transaksi — demonstrasi Encapsulation
 * Abstraction diterapkan melalui interface Kategorisasi
 */
public class Transaksi implements Kategorisasi {

    public enum JenisTransaksi { PEMASUKAN, PENGELUARAN }

    private int id;
    private String judul;
    private double jumlah;
    private JenisTransaksi jenis;
    private String kategori;
    private LocalDate tanggal;
    private String keterangan;
    private int userId;

    public Transaksi() {}

    public Transaksi(int id, String judul, double jumlah, JenisTransaksi jenis,
                     String kategori, LocalDate tanggal, String keterangan, int userId) {
        this.id = id;
        this.judul = judul;
        this.jumlah = jumlah;
        this.jenis = jenis;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.userId = userId;
    }

    // Implementasi interface Kategorisasi (Abstraction)
    @Override
    public String getKategori() { return kategori; }

    @Override
    public String getJenisLabel() {
        return jenis == JenisTransaksi.PEMASUKAN ? "Pemasukan" : "Pengeluaran";
    }

    // Getter & Setter (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public JenisTransaksi getJenis() { return jenis; }
    public void setJenis(JenisTransaksi jenis) { this.jenis = jenis; }

    public void setKategori(String kategori) { this.kategori = kategori; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
