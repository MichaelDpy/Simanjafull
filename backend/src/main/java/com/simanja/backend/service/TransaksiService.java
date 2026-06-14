package com.simanja.backend.service;

import com.simanja.backend.dto.request.TransaksiRequest;
import com.simanja.backend.dto.response.LaporanResponse;
import com.simanja.backend.dto.response.SummaryResponse;
import com.simanja.backend.dto.response.TransaksiResponse;
import com.simanja.backend.exception.ResourceNotFoundException;
import com.simanja.backend.exception.ValidationException;
import com.simanja.backend.model.Target;
import com.simanja.backend.model.Transaksi;
import com.simanja.backend.model.Transaksi.JenisTransaksi;
import com.simanja.backend.model.User;
import com.simanja.backend.repository.TargetRepository;
import com.simanja.backend.repository.TransaksiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TransaksiService — Service layer untuk logika bisnis transaksi.
 *
 * Demonstrasi:
 * - Service Layer (MVC)
 * - Validasi data (input + bisnis)
 * - Polymorphism (stream + lambda, penggunaan interface Kategorisasi)
 */
@Service
@Transactional
public class TransaksiService {

    private final TransaksiRepository transaksiRepository;
    private final TargetRepository targetRepository;
    private final UserService userService;

    public TransaksiService(TransaksiRepository transaksiRepository,
                            TargetRepository targetRepository,
                            UserService userService) {
        this.transaksiRepository = transaksiRepository;
        this.targetRepository    = targetRepository;
        this.userService         = userService;
    }

    // =============================================
    // READ
    // =============================================

    /**
     * Ambil semua transaksi milik user, urut tanggal terbaru.
     * Demonstrasi Polymorphism: stream + lambda + method reference.
     */
    @Transactional(readOnly = true)
    public List<TransaksiResponse> getAllByUser(String email) {
        User user = userService.getUserEntityByEmail(email);
        return transaksiRepository.findByUserIdOrderByTanggalDesc(user.getId())
                .stream()
                .map(TransaksiResponse::from)  // Polymorphism — method reference
                .collect(Collectors.toList());
    }

    /**
     * Ambil transaksi terbaru (n item).
     */
    @Transactional(readOnly = true)
    public List<TransaksiResponse> getRecentByUser(String email, int limit) {
        User user = userService.getUserEntityByEmail(email);
        return transaksiRepository.findByUserIdOrderByTanggalDesc(user.getId())
                .stream()
                .limit(limit)
                .map(TransaksiResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Ambil detail transaksi berdasarkan ID.
     */
    @Transactional(readOnly = true)
    public TransaksiResponse getById(Long id, String email) {
        Transaksi transaksi = findAndValidateOwnership(id, email);
        return TransaksiResponse.from(transaksi);
    }

    /**
     * Cari transaksi berdasarkan keyword judul.
     */
    @Transactional(readOnly = true)
    public List<TransaksiResponse> cari(String email, String keyword) {
        User user = userService.getUserEntityByEmail(email);
        return transaksiRepository
                .findByUserIdAndJudulContainingIgnoreCaseOrderByTanggalDesc(user.getId(), keyword)
                .stream()
                .map(TransaksiResponse::from)
                .collect(Collectors.toList());
    }

    // =============================================
    // SUMMARY / STATISTIK
    // =============================================

    /**
     * Ringkasan keuangan untuk dashboard.
     */
    @Transactional(readOnly = true)
    public SummaryResponse getSummary(String email) {
        User user = userService.getUserEntityByEmail(email);
        Long userId = user.getId();

        Double pemasukan  = transaksiRepository.sumJumlahByUserIdAndJenis(userId, JenisTransaksi.PEMASUKAN);
        Double pengeluaran = transaksiRepository.sumJumlahByUserIdAndJenis(userId, JenisTransaksi.PENGELUARAN);
        Double tabungan   = targetRepository.sumCurrentAmountByUserId(userId);

        long jumlahTransaksi = transaksiRepository.findByUserIdOrderByTanggalDesc(userId).size();
        long jumlahTarget    = targetRepository.findByUserIdOrderByAchievedAscDeadlineAsc(userId).size();

        double saldo = (pemasukan != null ? pemasukan : 0.0)
                     - (pengeluaran != null ? pengeluaran : 0.0);

        return new SummaryResponse(
                pemasukan  != null ? pemasukan  : 0.0,
                pengeluaran != null ? pengeluaran : 0.0,
                saldo,
                tabungan   != null ? tabungan   : 0.0,
                jumlahTransaksi,
                jumlahTarget
        );
    }

    /**
     * Data laporan per bulan dan per kategori.
     */
    @Transactional(readOnly = true)
    public LaporanResponse getLaporan(String email) {
        User user = userService.getUserEntityByEmail(email);
        Long userId = user.getId();

        Map<String, Double> pemasukanPerBulan   = buildPerBulanMap(userId, JenisTransaksi.PEMASUKAN);
        Map<String, Double> pengeluaranPerBulan = buildPerBulanMap(userId, JenisTransaksi.PENGELUARAN);
        Map<String, Double> perKategori         = buildPerKategoriMap(userId);

        return new LaporanResponse(pemasukanPerBulan, pengeluaranPerBulan, perKategori);
    }

    // =============================================
    // CREATE / UPDATE / DELETE
    // =============================================

    /**
     * Tambah transaksi baru dengan validasi.
     * Demonstrasi Validasi (input + bisnis).
     */
    public TransaksiResponse tambah(String email, TransaksiRequest request) {
        // Validasi bisnis — tanggal tidak boleh di masa depan
        if (request.getTanggal().isAfter(LocalDate.now())) {
            throw new ValidationException("Tanggal transaksi tidak boleh di masa depan");
        }

        User user = userService.getUserEntityByEmail(email);
        JenisTransaksi jenis = parseJenis(request.getJenis());

        Transaksi transaksi = new Transaksi(
                request.getJudul(),
                request.getJumlah(),
                jenis,
                request.getKategori(),
                request.getTanggal(),
                request.getKeterangan(),
                user
        );

        Transaksi saved = transaksiRepository.save(transaksi);
        return TransaksiResponse.from(saved);
    }

    /**
     * Update transaksi yang ada.
     */
    public TransaksiResponse update(Long id, String email, TransaksiRequest request) {
        // Validasi bisnis
        if (request.getTanggal().isAfter(LocalDate.now())) {
            throw new ValidationException("Tanggal transaksi tidak boleh di masa depan");
        }

        Transaksi transaksi = findAndValidateOwnership(id, email);
        JenisTransaksi jenis = parseJenis(request.getJenis());

        transaksi.setJudul(request.getJudul());
        transaksi.setJumlah(request.getJumlah());
        transaksi.setJenis(jenis);
        transaksi.setKategori(request.getKategori());
        transaksi.setTanggal(request.getTanggal());
        transaksi.setKeterangan(request.getKeterangan());

        Transaksi saved = transaksiRepository.save(transaksi);
        return TransaksiResponse.from(saved);
    }

    /**
     * Hapus transaksi.
     */
    public void hapus(Long id, String email) {
        Transaksi transaksi = findAndValidateOwnership(id, email);
        transaksiRepository.delete(transaksi);
    }

    // =============================================
    // Private Helpers
    // =============================================

    private Transaksi findAndValidateOwnership(Long id, String email) {
        Transaksi transaksi = transaksiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaksi", id));

        // Validasi kepemilikan (security check)
        if (!transaksi.getUser().getEmail().equals(email)) {
            throw new ValidationException("Anda tidak memiliki akses ke transaksi ini");
        }
        return transaksi;
    }

    private JenisTransaksi parseJenis(String jenis) {
        try {
            return JenisTransaksi.valueOf(jenis.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Jenis transaksi tidak valid. Gunakan PEMASUKAN atau PENGELUARAN");
        }
    }

    private Map<String, Double> buildPerBulanMap(Long userId, JenisTransaksi jenis) {
        String[] namaBulan = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agu","Sep","Okt","Nov","Des"};
        Map<String, Double> result = new LinkedHashMap<>();
        for (String b : namaBulan) result.put(b, 0.0);

        List<Object[]> rows = transaksiRepository.findJumlahPerBulan(userId, jenis);
        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            double total = ((Number) row[2]).doubleValue();
            result.put(namaBulan[month - 1], total);
        }
        return result;
    }

    private Map<String, Double> buildPerKategoriMap(Long userId) {
        Map<String, Double> result = new LinkedHashMap<>();
        List<Object[]> rows = transaksiRepository.findPengeluaranPerKategori(userId);
        for (Object[] row : rows) {
            String kategori = (String) row[0];
            double total = ((Number) row[1]).doubleValue();
            result.put(kategori, total);
        }
        return result;
    }
}
