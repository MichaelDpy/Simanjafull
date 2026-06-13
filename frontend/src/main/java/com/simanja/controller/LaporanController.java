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

import java.util.Map;

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

        loadBarChart(userId);
        loadPieChart(userId);
        loadStatistik(userId);
        loadInsights(userId);
    }

    private void loadBarChart(int userId) {
        Map<String, Double> pemasukan   = service.getPemasukanPerBulan(userId);
        Map<String, Double> pengeluaran = service.getPengeluaranPerBulan(userId);

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

    private void loadPieChart(int userId) {
        if (pieChart == null) return;
        Map<String, Double> perKategori = service.getPengeluaranPerKategori(userId);
        var data = FXCollections.<PieChart.Data>observableArrayList();
        perKategori.forEach((kat, val) -> data.add(new PieChart.Data(kat, val)));
        pieChart.setData(data);
        pieChart.setAnimated(false);
        pieChart.setLabelsVisible(false);
    }

    private void loadStatistik(int userId) {
        double totalPemasukan   = service.getTotalPemasukan(userId);
        double totalPengeluaran = service.getTotalPengeluaran(userId);
        double saldo            = service.getSaldo(userId);
        double rataPemasukan    = totalPemasukan / 6;
        double rataPengeluaran  = totalPengeluaran / 6;

        lblTotalPemasukan.setText(CurrencyFormatter.format(totalPemasukan));
        lblTotalPengeluaran.setText(CurrencyFormatter.format(totalPengeluaran));
        lblSaldo.setText(CurrencyFormatter.format(saldo));
        lblRataPemasukan.setText(CurrencyFormatter.format(rataPemasukan));
        lblRataPengeluaran.setText(CurrencyFormatter.format(rataPengeluaran));
    }

    private void loadInsights(int userId) {
        Map<String, Double> byKat = service.getPengeluaranPerKategori(userId);

        // Kategori terboros
        if (lblKategoriTerboros != null) {
            byKat.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> lblKategoriTerboros.setText(
                    "Bulan ini pengeluaran terbesarmu ada di kategori "
                    + e.getKey() + ", yaitu sebesar " + CurrencyFormatter.format(e.getValue()) + "."));
        }

        // Hari paling boros (dummy)
        if (lblHariTerboros != null) {
            lblHariTerboros.setText(
                "Kamu paling banyak mengeluarkan uang pada hari Sabtu " +
                "(Rata-rata Rp 300.000 per Sabtu).");
        }

        // Rata-rata harian (asumsi 30 hari)
        if (lblRataHarian != null) {
            double rataHarian = service.getTotalPengeluaran(userId) / 30;
            lblRataHarian.setText(
                "Rata-rata uang yang kamu habiskan setiap harinya di bulan ini adalah "
                + CurrencyFormatter.format(rataHarian) + "/hari.");
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
