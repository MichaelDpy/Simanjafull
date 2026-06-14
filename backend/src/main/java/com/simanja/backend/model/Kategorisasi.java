package com.simanja.backend.model;

/**
 * Interface Kategorisasi — demonstrasi Abstraction (PBO)
 * Mendefinisikan kontrak untuk objek yang memiliki kategori dan jenis label.
 * Diimplementasikan oleh Transaksi.
 */
public interface Kategorisasi {

    /**
     * Mengembalikan kategori dari objek.
     */
    String getKategori();

    /**
     * Mengembalikan label jenis (Pemasukan / Pengeluaran).
     */
    String getJenisLabel();
}
