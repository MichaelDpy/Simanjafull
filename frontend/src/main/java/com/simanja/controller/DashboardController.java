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

        // Summary
        double totalPemasukan   = transaksiService.getTotalPemasukan(userId);
        double totalPengeluaran = transaksiService.getTotalPengeluaran(userId);
        double saldo            = transaksiService.getSaldo(userId);
        double sisaBudget       = totalPemasukan - totalPengeluaran;

        lblSaldo.setText(CurrencyFormatter.format(saldo));
        lblTotalPemasukan.setText(CurrencyFormatter.format(totalPemasukan));
        lblTotalPengeluaran.setText(CurrencyFormatter.format(totalPengeluaran));
        if (lblSisaBudget != null) {
            lblSisaBudget.setText(CurrencyFormatter.format(Math.max(0, sisaBudget)));
        }

        loadLineChart(userId);
        loadPieChart(userId);
        loadTableTransaksi(userId);
        loadBudgetStatus();
        loadGoalsTracker(userId);
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

    private void loadLineChart(int userId) {
        Map<String, Double> pemasukan   = transaksiService.getPemasukanPerBulan(userId);
        Map<String, Double> pengeluaran = transaksiService.getPengeluaranPerBulan(userId);

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

    private void loadPieChart(int userId) {
        Map<String, Double> perKategori = transaksiService.getPengeluaranPerKategori(userId);
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

        // Dummy budget data
        String[][] budgets = {
            {"Jajan", "950000", "1000000", "95"},
            {"Groceries", "1800000", "2000000", "90"}
        };

        for (String[] b : budgets) {
            VBox item = new VBox(6);
            HBox header = new HBox();
            Label name = new Label(b[0] + " (Rp " + formatK(Double.parseDouble(b[1]))
                + "/" + formatK(Double.parseDouble(b[2])) + ")");
            name.getStyleClass().add("budget-item-label");
            HBox.setHgrow(name, Priority.ALWAYS);
            Label pct = new Label(b[3] + "%");
            pct.getStyleClass().add("budget-item-pct");
            header.getChildren().addAll(name, pct);

            ProgressBar pb = new ProgressBar(Double.parseDouble(b[3]) / 100.0);
            pb.prefWidthProperty().bind(budgetStatusContainer.widthProperty());
            pb.setPrefHeight(6);
            int pctVal = Integer.parseInt(b[3]);
            pb.getStyleClass().add(pctVal >= 90 ? "progress-danger" : "progress-orange");

            item.getChildren().addAll(header, pb);
            budgetStatusContainer.getChildren().add(item);
        }
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

        // Dummy tagihan
        String[][] tagihan = {
            {"▶", "Netflix",     "3 hari lagi",  "186000"},
            {"♪", "Spotify",     "5 hari lagi",  "55000"},
            {"⚡", "Electricity", "10 hari lagi", "450000"}
        };

        for (String[] t : tagihan) {
            HBox row = new HBox(10);
            row.getStyleClass().add("tagihan-row");
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 12, 10, 12));

            Label icon = new Label(t[0]);
            icon.getStyleClass().add("tagihan-icon");
            icon.setMinSize(32, 32);
            icon.setAlignment(Pos.CENTER);

            VBox info = new VBox(2);
            HBox.setHgrow(info, Priority.ALWAYS);
            Label name = new Label(t[1]);
            name.getStyleClass().add("tagihan-name");
            Label due = new Label(t[2]);
            due.getStyleClass().add("tagihan-due");
            info.getChildren().addAll(name, due);

            Label amount = new Label(CurrencyFormatter.format(Double.parseDouble(t[3])));
            amount.getStyleClass().add("tagihan-amount");

            row.getChildren().addAll(icon, info, amount);
            tagihanContainer.getChildren().add(row);
        }
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
