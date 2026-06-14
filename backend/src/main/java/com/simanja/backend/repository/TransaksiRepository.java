package com.simanja.backend.repository;

import com.simanja.backend.model.Transaksi;
import com.simanja.backend.model.Transaksi.JenisTransaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TransaksiRepository — Repository layer untuk akses data Transaksi.
 * Menggunakan Spring Data JPA dengan custom query untuk kebutuhan bisnis.
 */
@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {

    /**
     * Ambil semua transaksi milik user tertentu, urut tanggal terbaru.
     */
    List<Transaksi> findByUserIdOrderByTanggalDesc(Long userId);

    /**
     * Ambil transaksi berdasarkan user dan jenis.
     */
    List<Transaksi> findByUserIdAndJenis(Long userId, JenisTransaksi jenis);

    /**
     * Hitung total jumlah berdasarkan user dan jenis transaksi.
     */
    @Query("SELECT COALESCE(SUM(t.jumlah), 0) FROM Transaksi t WHERE t.user.id = :userId AND t.jenis = :jenis")
    Double sumJumlahByUserIdAndJenis(@Param("userId") Long userId, @Param("jenis") JenisTransaksi jenis);

    /**
     * Cari transaksi berdasarkan judul (case-insensitive).
     */
    List<Transaksi> findByUserIdAndJudulContainingIgnoreCaseOrderByTanggalDesc(Long userId, String judul);

    /**
     * Ambil pengeluaran per kategori untuk user tertentu.
     */
    @Query("SELECT t.kategori, SUM(t.jumlah) FROM Transaksi t " +
           "WHERE t.user.id = :userId AND t.jenis = 'PENGELUARAN' " +
           "GROUP BY t.kategori")
    List<Object[]> findPengeluaranPerKategori(@Param("userId") Long userId);

    /**
     * Ambil pemasukan per bulan untuk laporan.
     */
    @Query("SELECT MONTH(t.tanggal), YEAR(t.tanggal), SUM(t.jumlah) FROM Transaksi t " +
           "WHERE t.user.id = :userId AND t.jenis = :jenis " +
           "GROUP BY YEAR(t.tanggal), MONTH(t.tanggal) " +
           "ORDER BY YEAR(t.tanggal), MONTH(t.tanggal)")
    List<Object[]> findJumlahPerBulan(@Param("userId") Long userId, @Param("jenis") JenisTransaksi jenis);
}
