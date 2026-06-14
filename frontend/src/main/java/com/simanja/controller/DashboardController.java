package com.simanja.controller;

import com.simanja.model.Target;
import com.simanja.model.Transaksi;
import com.simanja.model.User;
import com.simanja.service.TargetService;
import com.simanja.service.TransaksiService;
import com.simanja.util.CurrencyFormatter;
import com.simanja.util.SceneManager;
import com.simanja.util.SessionManager;
import com.simanja.util.SidebarToggle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Controller untuk Dashboard
 * Demonstrasi: MVC, Chart JavaFX, data binding
 */
public class DashboardController {

    // --- Summary Cards ---
    @FXML private Label lblNamaDashboard;
    @FXML private Label lblAvatarDashboard;
    @FXML private ImageView imgAvatarDashboard;
    @FXML private Label lblSaldo;
    @FXML private Label lblTotalPemasukan;
    @FXML private Label lblTotalPengeluaran;
    @FXML private Label lblSisaBudget;

    // --- Line Chart ---
    @FXML private LineChart<String, Number> lineChart;
    @FXML private CategoryAxis xAxisLine;
    @FXML private NumberAxis yAxisLine;

    // --- Pie Chart ---
    @FXML private PieChart pieChart;

    // --- Tabel transaksi terbaru ---
    @FXML private TableView<Transaksi> tableTransaksi;
    @FXML private TableColumn<Transaksi, String> colTanggal;
    @FXML private TableColumn<Transaksi, String> colJudul;
    @FXML private TableColumn<Transaksi, String> colKategori;
    @FXML private TableColumn<Transaksi, String> colJumlah;

    // --- Budget & Goals containers ---
    @FXML private VBox budgetStatusContainer;
    @FXML private VBox goalsTrackerContainer;
    @FXML private VBox tagihanContainer;

    // --- Hamburger ---
    @FXML private VBox sidebar;

    private final SidebarToggle sidebarToggle    = new SidebarToggle();
    private final TransaksiService transaksiService = new TransaksiService();
    private final TargetService targetService       = new TargetService();
    private final DateTimeFormatter dtf             = DateTimeFormatter.ofPattern("dd MMM");

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            SceneManager.switchTo("login");
            return;
        }

        int userId = user.getId();
        if (lblNamaDashboard != null) lblNamaDashboard.setText(user.getNama());
        applyProfileAvatar(user);

        try {
            // Summary — panggil sekali, gunakan hasilnya untuk semua label
            TransaksiService.SummaryResponse summary = transaksiService.getSummaryResponse();
            double totalPemasukan   = summary.totalPemasukan();
            double totalPengeluaran = summary.totalPengeluaran();
            double saldo            = summary.saldo();
            double sisaBudget       = totalPemasukan - totalPengeluaran;

            lblSaldo.setText(CurrencyFormatter.format(saldo));
            lblTotalPemasukan.setText(CurrencyFormatter.format(totalPemasukan));
            lblTotalPengeluaran.setText(CurrencyFormatter.format(totalPengeluaran));
            if (lblSisaBudget != null) {
                lblSisaBudget.setText(CurrencyFormatter.format(Math.max(0, sisaBudget)));
            }

            // Laporan — panggil sekali untuk chart
            TransaksiService.LaporanResponse laporan = transaksiService.getLaporanResponse();
            loadLineChart(laporan);
            loadPieChart(laporan);

            loadTableTransaksi(userId);
        } catch (Exception e) {
            System.err.println("Gagal memuat data dashboard: " + e.getMessage());
            lblSaldo.setText("Rp 0");
            lblTotalPemasukan.setText("Rp 0");
            lblTotalPengeluaran.setText("Rp 0");
        }

        loadBudgetStatus();

        try {
            loadGoalsTracker(userId);
        } catch (Exception e) {
            System.err.println("Gagal memuat goals: " + e.getMessage());
        }

        loadTagihanMendatang();
    }

    /** Tampilkan foto profil atau fallback inisial di header dashboard */
    private void applyProfileAvatar(User user) {
        if (lblAvatarDashboard == null || imgAvatarDashboard == null) return;

        // Set inisial sebagai fallback
        String initial = user.getNama() != null && !user.getNama().isEmpty()
            ? String.valueOf(user.getNama().charAt(0)).toUpperCase() : "U";
        lblAvatarDashboard.setText(initial);

        String path = user.getProfileImagePath();
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    Image img = new Image(file.toURI().toString(), 32, 32, false, true);
                    if (!img.isError()) {
                        imgAvatarDashboard.setImage(img);
                        imgAvatarDashboard.setVisible(true);
                        imgAvatarDashboard.setManaged(true);
                        lblAvatarDashboard.setVisible(false);
                        lblAvatarDashboard.setManaged(false);
                        return;
                    }
                } catch (Exception ignored) {}
            }
        }
        // Fallback: tampilkan inisial
        imgAvatarDashboard.setVisible(false);
        imgAvatarDashboard.setManaged(false);
        lblAvatarDashboard.setVisible(true);
        lblAvatarDashboard.setManaged(true);
    }

    private void loadLineChart(TransaksiService.LaporanResponse laporan) {
        Map<String, Double> pemasukan   = laporan.pemasukanPerBulan();
        Map<String, Double> pengeluaran = laporan.pengeluaranPerBulan();

        XYChart.Series<String, Number> seriPemasukan   = new XYChart.Series<>();
        XYChart.Series<String, Number> seriPengeluaran = new XYChart.Series<>();
        seriPemasukan.setName("Income");
        seriPengeluaran.setName("Expense");

        pemasukan.forEach((b, v)   -> seriPemasukan.getData().add(new XYChart.Data<>(b, v)));
        pengeluaran.forEach((b, v) -> seriPengeluaran.getData().add(new XYChart.Data<>(b, v)));

        lineChart.getData().setAll(
            java.util.List.of(seriPemasukan, seriPengeluaran));
        lineChart.setAnimated(false);
    }

    private void loadPieChart(TransaksiService.LaporanResponse laporan) {
        Map<String, Double> perKategori = laporan.pengeluaranPerKategori();
        var data = FXCollections.<PieChart.Data>observableArrayList();
        perKategori.forEach((kat, val) -> data.add(new PieChart.Data(kat, val)));
        pieChart.setData(data);
        pieChart.setAnimated(false);
    }

    private void loadTableTransaksi(int userId) {
        List<Transaksi> recent = transaksiService.getRecentByUser(userId, 5);

        // Kolom mengisi penuh lebar tabel — tidak ada celah putih di kanan
        tableTransaksi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colTanggal.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getTanggal().format(dtf)));
        colKategori.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getKategori()));
        colJudul.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getJudul()));
        colJumlah.setCellValueFactory(cell -> {
            Transaksi t = cell.getValue();
            boolean inc = t.getJenis() == Transaksi.JenisTransaksi.PEMASUKAN;
            return new SimpleStringProperty((inc ? "+ " : "- ") + CurrencyFormatter.format(t.getJumlah()));
        });

        // Warna jumlah berdasarkan jenis
        colJumlah.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    setStyle(item.startsWith("+")
                        ? "-fx-text-fill: #00e5a0;" : "-fx-text-fill: #ff8c42;");
                }
            }
        });

        tableTransaksi.setItems(FXCollections.observableArrayList(recent));
    }

    private void loadBudgetStatus() {
        if (budgetStatusContainer == null) return;
        budgetStatusContainer.getChildren().clear();

        Label emptyBudget = new Label("Fitur Limit Budget akan segera hadir.");
        emptyBudget.setStyle("-fx-text-fill: #a0aabf; -fx-font-style: italic;");
        budgetStatusContainer.getChildren().add(emptyBudget);
    }

    private void loadGoalsTracker(int userId) {
        if (goalsTrackerContainer == null) return;
        goalsTrackerContainer.getChildren().clear();

        List<Target> targets = targetService.getAllByUser(userId);
        targets.stream().filter(t -> !t.isAchieved()).limit(2).forEach(t -> {
            VBox item = new VBox(6);
            double pct = t.getProgressPercent();

            HBox header = new HBox();
            String shortName = t.getNama() + " (Rp "
                + formatK(t.getCurrentAmount()) + "/" + formatK(t.getTargetAmount()) + ")";
            Label name = new Label(shortName);
            name.getStyleClass().add("budget-item-label");
            HBox.setHgrow(name, Priority.ALWAYS);
            Label pctLbl = new Label(String.format("%.1f%%", pct));
            pctLbl.getStyleClass().add("progress-pct-label");
            header.getChildren().addAll(name, pctLbl);

            ProgressBar pb = new ProgressBar(pct / 100.0);
            pb.prefWidthProperty().bind(goalsTrackerContainer.widthProperty());
            pb.setPrefHeight(6);
            pb.getStyleClass().add("target-progress");

            item.getChildren().addAll(header, pb);
            goalsTrackerContainer.getChildren().add(item);
        });
    }

    private void loadTagihanMendatang() {
        if (tagihanContainer == null) return;
        tagihanContainer.getChildren().clear();

        Label emptyTagihan = new Label("Tidak ada tagihan mendatang saat ini.");
        emptyTagihan.setStyle("-fx-text-fill: #a0aabf; -fx-font-style: italic;");
        tagihanContainer.getChildren().add(emptyTagihan);
    }

    private String formatK(double val) {
        if (val >= 1_000_000) return String.format("%.1fM", val / 1_000_000);
        if (val >= 1_000)     return String.format("%.0fk", val / 1_000);
        return String.format("%.0f", val);
    }

    // --- Navigasi Sidebar ---
    @FXML private void handleToggleSidebar()  { sidebarToggle.toggle(sidebar); }
    @FXML private void handleDashboard()      { SceneManager.switchTo("dashboard"); }
    @FXML private void handleTransaksi()      { SceneManager.switchTo("transaksi"); }
    @FXML private void handleTarget()         { SceneManager.switchTo("target"); }
    @FXML private void handleLaporan()        { SceneManager.switchTo("laporan"); }
    @FXML private void handleProfil()         { SceneManager.switchTo("profil"); }
    @FXML private void handleCatatTransaksi() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/form-transaksi.fxml"));
            Parent root = loader.load();
            FormTransaksiController ctrl = loader.getController();
            ctrl.setMode(FormTransaksiController.Mode.TAMBAH, null);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(SceneManager.getStage());
            dialog.setTitle("Catat Transaksi Baru");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/styles/dark-theme.css").toExternalForm());
            dialog.setScene(scene);
            dialog.showAndWait();
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.switchTo("landing");
    }
}
