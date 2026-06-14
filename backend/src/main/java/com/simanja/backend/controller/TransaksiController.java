package com.simanja.backend.controller;

import com.simanja.backend.dto.request.TransaksiRequest;
import com.simanja.backend.dto.response.LaporanResponse;
import com.simanja.backend.dto.response.SummaryResponse;
import com.simanja.backend.dto.response.TransaksiResponse;
import com.simanja.backend.service.TransaksiService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TransaksiController — REST Controller untuk CRUD transaksi dan laporan.
 *
 * Demonstrasi: Controller layer (MVC), Security (JWT authenticated)
 */
@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {

    private final TransaksiService transaksiService;

    public TransaksiController(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
    }

    /**
     * GET /api/transaksi
     * Ambil semua transaksi milik user yang login.
     * Query param ?keyword= untuk pencarian.
     */
    @GetMapping
    public ResponseEntity<List<TransaksiResponse>> getAll(
            Authentication authentication,
            @RequestParam(required = false) String keyword) {

        String email = authentication.getName();
        List<TransaksiResponse> list;

        if (keyword != null && !keyword.isBlank()) {
            list = transaksiService.cari(email, keyword);
        } else {
            list = transaksiService.getAllByUser(email);
        }

        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/transaksi/recent?limit=5
     * Ambil transaksi terbaru (untuk dashboard).
     */
    @GetMapping("/recent")
    public ResponseEntity<List<TransaksiResponse>> getRecent(
            Authentication authentication,
            @RequestParam(defaultValue = "5") int limit) {

        String email = authentication.getName();
        List<TransaksiResponse> list = transaksiService.getRecentByUser(email, limit);
        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/transaksi/summary
     * Ringkasan keuangan — total pemasukan, pengeluaran, saldo, tabungan.
     */
    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(Authentication authentication) {
        String email = authentication.getName();
        SummaryResponse summary = transaksiService.getSummary(email);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/transaksi/laporan
     * Data laporan per bulan dan per kategori (untuk grafik).
     */
    @GetMapping("/laporan")
    public ResponseEntity<LaporanResponse> getLaporan(Authentication authentication) {
        String email = authentication.getName();
        LaporanResponse laporan = transaksiService.getLaporan(email);
        return ResponseEntity.ok(laporan);
    }

    /**
     * GET /api/transaksi/{id}
     * Detail transaksi berdasarkan ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransaksiResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        TransaksiResponse response = transaksiService.getById(id, email);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/transaksi
     * Tambah transaksi baru.
     * @Valid memastikan Bean Validation dijalankan.
     */
    @PostMapping
    public ResponseEntity<TransaksiResponse> tambah(
            @Valid @RequestBody TransaksiRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        TransaksiResponse response = transaksiService.tambah(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/transaksi/{id}
     * Update transaksi yang ada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransaksiResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TransaksiRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        TransaksiResponse response = transaksiService.update(id, email, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/transaksi/{id}
     * Hapus transaksi.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> hapus(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        transaksiService.hapus(id, email);
        return ResponseEntity.ok(Map.of("message", "Transaksi berhasil dihapus"));
    }
}
