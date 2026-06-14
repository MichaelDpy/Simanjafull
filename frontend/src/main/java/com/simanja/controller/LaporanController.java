package com.simanja.controller;

import com.simanja.service.TransaksiService;
import com.simanja.util.CurrencyFormatter;
import com.simanja.util.SceneManager;
import com.simanja.util.SessionManager;
import com.simanja.util.SidebarToggle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller untuk Laporan Keuangan
 * Demonstrasi: BarChart, PieChart, statistik agregat, Insight Pintar
 */
public class LaporanController {

    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private PieChart pieChart;

    @FXML private Label lblTotalPemasukan;
    @FXML private Label lblTotalPengeluaran;
    @FXML private Label lblSaldo;
    @FXML private Label lblRataPemasukan;
    @FXML private Label lblRataPengeluaran;

    @FXML private Label lblKategoriTerboros;
    @FXML private Label lblHariTerboros;
    @FXML private Label lblRataHarian;

    @FXML private VBox sidebar;

    private final TransaksiService service    = new TransaksiService();
    private final SidebarToggle sidebarToggle = new SidebarToggle();

    @FXML
    public void initialize() {
        if (SessionManager.getInstance().getCurrentUser() == null) {
            SceneManager.switchTo("login");
            return;
        }

        int userId = SessionManager.getInstance().getCurrentUser().getId();

        try {
            // Panggil laporan sekali, gunakan hasilnya untuk semua chart
            TransaksiService.LaporanResponse laporan = service.getLaporanResponse();
            loadBarChart(laporan);
            loadPieChart(laporan);

            // Panggil summary sekali untuk statistik
            TransaksiService.SummaryResponse summary = service.getSummaryResponse();
            loadStatistik(summary);

            loadInsights(userId, laporan);
        } catch (Exception e) {
            System.err.println("Gagal memuat data laporan: " + e.getMessage());
            // Set default values jika backend tidak tersedia
            lblTotalPemasukan.setText("Rp 0");
            lblTotalPengeluaran.setText("Rp 0");
            lblSaldo.setText("Rp 0");
            if (lblRataPemasukan != null) lblRataPemasukan.setText("Rp 0");
            if (lblRataPengeluaran != null) lblRataPengeluaran.setText("Rp 0");
        }
    }

    private void loadBarChart(TransaksiService.LaporanResponse laporan) {
        Map<String, Double> pemasukan   = laporan.pemasukanPerBulan();
        Map<String, Double> pengeluaran = laporan.pengeluaranPerBulan();

        XYChart.Series<String, Number> seriPemasukan   = new XYChart.Series<>();
        XYChart.Series<String, Number> seriPengeluaran = new XYChart.Series<>();
        seriPemasukan.setName("Pemasukan");
        seriPengeluaran.setName("Pengeluaran");

        pemasukan.forEach((b, v)   -> seriPemasukan.getData().add(new XYChart.Data<>(b, v)));
        pengeluaran.forEach((b, v) -> seriPengeluaran.getData().add(new XYChart.Data<>(b, v)));

        barChart.getData().setAll(
            java.util.List.of(seriPemasukan, seriPengeluaran));
        barChart.setAnimated(false);
        barChart.setCategoryGap(16);
        barChart.setBarGap(4);
    }

    private void loadPieChart(TransaksiService.LaporanResponse laporan) {
        if (pieChart == null) return;
        Map<String, Double> perKategori = laporan.pengeluaranPerKategori();
        var data = FXCollections.<PieChart.Data>observableArrayList();
        perKategori.forEach((kat, val) -> data.add(new PieChart.Data(kat, val)));
        pieChart.setData(data);
        pieChart.setAnimated(false);
        pieChart.setLabelsVisible(false);
    }

    private void loadStatistik(TransaksiService.SummaryResponse summary) {
        double totalPemasukan   = summary.totalPemasukan();
        double totalPengeluaran = summary.totalPengeluaran();
        double saldo            = summary.saldo();
        double rataPemasukan    = totalPemasukan / 6;
        double rataPengeluaran  = totalPengeluaran / 6;

        lblTotalPemasukan.setText(CurrencyFormatter.format(totalPemasukan));
        lblTotalPengeluaran.setText(CurrencyFormatter.format(totalPengeluaran));
        lblSaldo.setText(CurrencyFormatter.format(saldo));
        lblRataPemasukan.setText(CurrencyFormatter.format(rataPemasukan));
        lblRataPengeluaran.setText(CurrencyFormatter.format(rataPengeluaran));
    }

    private void loadInsights(int userId, TransaksiService.LaporanResponse laporan) {
        Map<String, Double> byKat = laporan.pengeluaranPerKategori();

        // Kategori terboros
        if (lblKategoriTerboros != null) {
            byKat.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> lblKategoriTerboros.setText(
                    "Bulan ini pengeluaran terbesarmu ada di kategori "
                    + e.getKey() + ", yaitu sebesar " + CurrencyFormatter.format(e.getValue()) + "."));
        }

        // Hari paling boros (Dihitung dari transaksi bulan ini)
        if (lblHariTerboros != null) {
            try {
                List<com.simanja.model.Transaksi> semuaTrans = service.getAllByUser(userId);
                Map<DayOfWeek, Double> byDay = semuaTrans.stream()
                    .filter(t -> t.getJenis() == com.simanja.model.Transaksi.JenisTransaksi.PENGELUARAN)
                    .collect(Collectors.groupingBy(
                        t -> t.getTanggal().getDayOfWeek(),
                        Collectors.summingDouble(com.simanja.model.Transaksi::getJumlah)
                    ));

                byDay.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresentOrElse(e -> {
                        String hari = e.getKey().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
                        lblHariTerboros.setText("Kamu paling banyak mengeluarkan uang pada hari " + hari + ".");
                    }, () -> {
                        lblHariTerboros.setText("Belum ada data pengeluaran yang cukup.");
                    });
            } catch (Exception e) {
                lblHariTerboros.setText("Belum ada data pengeluaran yang cukup.");
            }
        }

        // Rata-rata harian (asumsi 30 hari)
        if (lblRataHarian != null) {
            try {
                TransaksiService.SummaryResponse summary = service.getSummaryResponse();
                double rataHarian = summary.totalPengeluaran() / 30;
                lblRataHarian.setText(
                    "Rata-rata uang yang kamu habiskan setiap harinya di bulan ini adalah "
                    + CurrencyFormatter.format(rataHarian) + "/hari.");
            } catch (Exception e) {
                lblRataHarian.setText("Data rata-rata harian belum tersedia.");
            }
        }
    }

    // --- Navigasi Sidebar ---
    @FXML private void handleToggleSidebar()   { sidebarToggle.toggle(sidebar); }
    @FXML private void handleDashboard()       { SceneManager.switchTo("dashboard"); }
    @FXML private void handleTransaksi()       { SceneManager.switchTo("transaksi"); }
    @FXML private void handleTarget()          { SceneManager.switchTo("target"); }
    @FXML private void handleLaporan()         { SceneManager.switchTo("laporan"); }
    @FXML private void handleProfil()         { SceneManager.switchTo("profil"); }

    @FXML private void handleCatatTransaksi()  { SceneManager.switchTo("transaksi"); }
    @FXML private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.switchTo("landing");
    }
}
