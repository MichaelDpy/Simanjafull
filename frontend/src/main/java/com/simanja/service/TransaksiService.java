package com.simanja.service;

import com.simanja.model.Transaksi;
import com.simanja.model.Transaksi.JenisTransaksi;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TransaksiService — Service layer untuk logika bisnis transaksi
 * Demonstrasi: Service layer, Polymorphism (stream + lambda), Validasi
 */
public class TransaksiService {

    // Data dummy transaksi
    private static final List<Transaksi> dummyTransaksi = new ArrayList<>();
    private static int nextId = 10;

    static {
        dummyTransaksi.add(new Transaksi(1, "Gaji Bulanan",      8_500_000, JenisTransaksi.PEMASUKAN,   "Gaji",        LocalDate.of(2025, 6, 1),  "Gaji bulan Juni", 2));
        dummyTransaksi.add(new Transaksi(2, "Belanja Bulanan",   1_200_000, JenisTransaksi.PENGELUARAN, "Kebutuhan",   LocalDate.of(2025, 6, 2),  "Supermarket",     2));
        dummyTransaksi.add(new Transaksi(3, "Bayar Listrik",       450_000, JenisTransaksi.PENGELUARAN, "Tagihan",     LocalDate.of(2025, 6, 3),  "PLN",             2));
        dummyTransaksi.add(new Transaksi(4, "Freelance Web",    2_500_000, JenisTransaksi.PEMASUKAN,   "Freelance",   LocalDate.of(2025, 6, 5),  "Project klien A", 2));
        dummyTransaksi.add(new Transaksi(5, "Makan Siang",          85_000, JenisTransaksi.PENGELUARAN, "Makanan",     LocalDate.of(2025, 6, 6),  "Resto XYZ",       2));
        dummyTransaksi.add(new Transaksi(6, "Transport",           350_000, JenisTransaksi.PENGELUARAN, "Transportasi",LocalDate.of(2025, 6, 7),  "Bensin & parkir", 2));
        dummyTransaksi.add(new Transaksi(7, "Investasi Reksa",  1_000_000, JenisTransaksi.PENGELUARAN, "Investasi",   LocalDate.of(2025, 6, 8),  "Bibit app",       2));
        dummyTransaksi.add(new Transaksi(8, "Bonus Proyek",     3_000_000, JenisTransaksi.PEMASUKAN,   "Bonus",       LocalDate.of(2025, 6, 10), "Klien B",         2));
        dummyTransaksi.add(new Transaksi(9, "Langganan Netflix",    54_000, JenisTransaksi.PENGELUARAN, "Hiburan",     LocalDate.of(2025, 6, 11), "Netflix",         2));
    }

    /** Ambil semua transaksi milik user */
    public List<Transaksi> getAllByUser(int userId) {
        return dummyTransaksi.stream()
            .filter(t -> t.getUserId() == userId)
            .sorted(Comparator.comparing(Transaksi::getTanggal).reversed())
            .collect(Collectors.toList());
    }

    /** Ambil transaksi terbaru (5 item) */
    public List<Transaksi> getRecentByUser(int userId, int limit) {
        return getAllByUser(userId).stream()
            .limit(limit)
            .collect(Collectors.toList());
    }

    /** Total pemasukan */
    public double getTotalPemasukan(int userId) {
        return dummyTransaksi.stream()
            .filter(t -> t.getUserId() == userId && t.getJenis() == JenisTransaksi.PEMASUKAN)
            .mapToDouble(Transaksi::getJumlah)
            .sum();
    }

    /** Total pengeluaran */
    public double getTotalPengeluaran(int userId) {
        return dummyTransaksi.stream()
            .filter(t -> t.getUserId() == userId && t.getJenis() == JenisTransaksi.PENGELUARAN)
            .mapToDouble(Transaksi::getJumlah)
            .sum();
    }

    /** Saldo = pemasukan - pengeluaran */
    public double getSaldo(int userId) {
        return getTotalPemasukan(userId) - getTotalPengeluaran(userId);
    }

    /** Jumlah transaksi per kategori */
    public Map<String, Double> getPengeluaranPerKategori(int userId) {
        return dummyTransaksi.stream()
            .filter(t -> t.getUserId() == userId && t.getJenis() == JenisTransaksi.PENGELUARAN)
            .collect(Collectors.groupingBy(
                Transaksi::getKategori,
                Collectors.summingDouble(Transaksi::getJumlah)
            ));
    }

    /** Tambah transaksi baru dengan validasi */
    public Transaksi tambah(String judul, double jumlah, JenisTransaksi jenis,
                            String kategori, LocalDate tanggal, String keterangan, int userId) {
        // Validasi
        if (judul == null || judul.isBlank())
            throw new IllegalArgumentException("Judul transaksi tidak boleh kosong.");
        if (jumlah <= 0)
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        if (kategori == null || kategori.isBlank())
            throw new IllegalArgumentException("Kategori tidak boleh kosong.");
        if (tanggal == null)
            throw new IllegalArgumentException("Tanggal tidak boleh kosong.");
        if (tanggal.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Tanggal tidak boleh di masa depan.");

        Transaksi t = new Transaksi(nextId++, judul, jumlah, jenis, kategori, tanggal, keterangan, userId);
        dummyTransaksi.add(t);
        return t;
    }

    /** Update transaksi */
    public void update(int id, String judul, double jumlah, JenisTransaksi jenis,
                       String kategori, LocalDate tanggal, String keterangan) {
        if (judul == null || judul.isBlank())
            throw new IllegalArgumentException("Judul tidak boleh kosong.");
        if (jumlah <= 0)
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");

        dummyTransaksi.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .ifPresent(t -> {
                t.setJudul(judul);
                t.setJumlah(jumlah);
                t.setJenis(jenis);
                t.setKategori(kategori);
                t.setTanggal(tanggal);
                t.setKeterangan(keterangan);
            });
    }

    /** Hapus transaksi */
    public void hapus(int id) {
        dummyTransaksi.removeIf(t -> t.getId() == id);
    }

    /** Cari transaksi berdasarkan judul */
    public List<Transaksi> cari(int userId, String keyword) {
        return getAllByUser(userId).stream()
            .filter(t -> t.getJudul().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    /** Data per bulan untuk grafik laporan */
    public Map<String, Double> getPemasukanPerBulan(int userId) {
        Map<String, Double> map = new LinkedHashMap<>();
        String[] bulan = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agu","Sep","Okt","Nov","Des"};
        // Dummy data untuk chart
        double[] values = {5_000_000, 6_500_000, 7_000_000, 8_000_000, 7_500_000, 11_500_000,
                           0, 0, 0, 0, 0, 0};
        for (int i = 0; i < bulan.length; i++) {
            map.put(bulan[i], values[i]);
        }
        return map;
    }

    public Map<String, Double> getPengeluaranPerBulan(int userId) {
        Map<String, Double> map = new LinkedHashMap<>();
        String[] bulan = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agu","Sep","Okt","Nov","Des"};
        double[] values = {3_000_000, 3_200_000, 4_500_000, 3_800_000, 4_200_000, 2_139_000,
                           0, 0, 0, 0, 0, 0};
        for (int i = 0; i < bulan.length; i++) {
            map.put(bulan[i], values[i]);
        }
        return map;
    }
}
