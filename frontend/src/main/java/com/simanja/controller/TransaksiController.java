package com.simanja.controller;

import com.simanja.model.Transaksi;
import com.simanja.model.Transaksi.JenisTransaksi;
import com.simanja.service.TransaksiService;
import com.simanja.util.CurrencyFormatter;
import com.simanja.util.SceneManager;
import com.simanja.util.SessionManager;
import com.simanja.util.SidebarToggle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller untuk halaman Daftar Transaksi
 * Demonstrasi: MVC, CRUD, Filter/Search, custom list per tanggal
 */
public class TransaksiController {

    @FXML private TextField txtCari;
    @FXML private ComboBox<String> cbFilter;
    @FXML private Button btnTambah;
    @FXML private VBox transaksiListContainer;

    @FXML private Label lblTotalPemasukan;
    @FXML private Label lblTotalPengeluaran;
    @FXML private Label lblDateRange;
    @FXML private ProgressBar progressPemasukan;
    @FXML private ProgressBar progressPengeluaran;
    @FXML private VBox topKategoriContainer;
    @FXML private VBox sidebar;

    private final TransaksiService service    = new TransaksiService();
    private final DateTimeFormatter dtfDay    = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"));
    private final DateTimeFormatter dtfMonth  = DateTimeFormatter.ofPattern("dd/MM/yy");
    private final SidebarToggle sidebarToggle = new SidebarToggle();

    @FXML
    public void initialize() {
        if (SessionManager.getInstance().getCurrentUser() == null) {
            SceneManager.switchTo("login");
            return;
        }

        cbFilter.setItems(FXCollections.observableArrayList("Semua", "Pemasukan", "Pengeluaran"));
        cbFilter.setValue("Semua");

        // Date range label bulan ini
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        if (lblDateRange != null) {
            lblDateRange.setText(start.format(dtfMonth) + " – " + end.format(dtfMonth));
        }

        loadData();
        cbFilter.setOnAction(e -> applyFilter());
        txtCari.setOnKeyReleased(e -> applyFilter());
    }

    private void loadData() {
        try {
            int userId = SessionManager.getInstance().getCurrentUser().getId();
            List<Transaksi> list = service.getAllByUser(userId);
            renderTransaksiList(list);
            updateSummary(list);
            renderTopKategori(userId);
        } catch (Exception e) {
            System.err.println("Gagal memuat data transaksi: " + e.getMessage());
            transaksiListContainer.getChildren().clear();
            Label error = new Label("Gagal memuat data. Pastikan server backend berjalan.");
            error.getStyleClass().add("page-subtitle");
            error.setPadding(new Insets(24));
            transaksiListContainer.getChildren().add(error);
        }
    }

    private void renderTransaksiList(List<Transaksi> list) {
        transaksiListContainer.getChildren().clear();

        // Group by tanggal
        Map<LocalDate, List<Transaksi>> grouped = list.stream()
            .collect(Collectors.groupingBy(Transaksi::getTanggal,
                LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<LocalDate, List<Transaksi>> entry : grouped.entrySet()) {
            // Date header
            Label dayLabel = new Label(entry.getKey().format(dtfDay).toUpperCase());
            dayLabel.getStyleClass().add("transaksi-date-header");
            VBox.setMargin(dayLabel, new Insets(12, 0, 4, 0));
            transaksiListContainer.getChildren().add(dayLabel);

            for (Transaksi t : entry.getValue()) {
                transaksiListContainer.getChildren().add(buildTransaksiRow(t));
            }
        }

        if (list.isEmpty()) {
            Label empty = new Label("Belum ada transaksi.");
            empty.getStyleClass().add("page-subtitle");
            empty.setPadding(new Insets(24));
            transaksiListContainer.getChildren().add(empty);
        }
    }

    private HBox buildTransaksiRow(Transaksi t) {
        HBox row = new HBox(14);
        row.getStyleClass().add("transaksi-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 16, 14, 16));

        // Category icon circle
        Label icon = new Label(getCategoryIcon(t.getKategori()));
        icon.getStyleClass().add("transaksi-icon");
        icon.setMinSize(40, 40);
        icon.setAlignment(Pos.CENTER);

        // Info
        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label title = new Label(t.getJudul());
        title.getStyleClass().add("transaksi-row-title");
        String subInfo = t.getKategori() + "  ·  " + (t.getKeterangan() != null && !t.getKeterangan().isEmpty()
            ? t.getKeterangan() : t.getJenis() == JenisTransaksi.PEMASUKAN ? "Pemasukan" : "Pengeluaran");
        Label sub = new Label(subInfo);
        sub.getStyleClass().add("transaksi-row-sub");
        info.getChildren().addAll(title, sub);

        // Amount
        boolean isPemasukan = t.getJenis() == JenisTransaksi.PEMASUKAN;
        Label amount = new Label((isPemasukan ? "+ " : "- ") + CurrencyFormatter.format(t.getJumlah()));
        amount.getStyleClass().add(isPemasukan ? "transaksi-amount-green" : "transaksi-amount-red");

        // Menu button
        Button menu = new Button("⋮");
        menu.getStyleClass().add("btn-row-menu");
        menu.setOnAction(e -> showContextMenu(t, menu));

        row.getChildren().addAll(icon, info, amount, menu);
        return row;
    }

    private void showContextMenu(Transaksi t, Button anchor) {
        ContextMenu cm = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        MenuItem hapus = new MenuItem("Hapus");
        edit.setOnAction(e -> openFormEdit(t));
        hapus.setOnAction(e -> konfirmasiHapus(t));
        cm.getItems().addAll(edit, hapus);
        cm.show(anchor, javafx.geometry.Side.BOTTOM, 0, 0);
    }

    private String getCategoryIcon(String kategori) {
        if (kategori == null) return "💡";
        String k = kategori.toLowerCase();
        if (k.contains("makan") || k.contains("food")) return "🍔";
        if (k.contains("transport")) return "🚗";
        if (k.contains("belanja") || k.contains("shopping")) return "🛒";
        if (k.contains("tagihan") || k.contains("listrik") || k.contains("pln")) return "🏠";
        if (k.contains("hiburan") || k.contains("netflix")) return "🎮";
        if (k.contains("gaji") || k.contains("salary")) return "💰";
        if (k.contains("freelance")) return "💼";
        if (k.contains("bonus")) return "🎁";
        if (k.contains("investasi")) return "📈";
        if (k.contains("pendidikan")) return "📚";
        if (k.contains("kesehatan")) return "🏥";
        return "💡";
    }

    private void applyFilter() {
        int userId   = SessionManager.getInstance().getCurrentUser().getId();
        String filter = cbFilter.getValue();
        String keyword = txtCari.getText().trim();

        List<Transaksi> all = service.getAllByUser(userId);
        List<Transaksi> filtered = all.stream()
            .filter(t -> {
                boolean matchJenis = filter.equals("Semua")
                    || (filter.equals("Pemasukan")   && t.getJenis() == JenisTransaksi.PEMASUKAN)
                    || (filter.equals("Pengeluaran") && t.getJenis() == JenisTransaksi.PENGELUARAN);
                boolean matchKeyword = keyword.isEmpty()
                    || t.getJudul().toLowerCase().contains(keyword.toLowerCase())
                    || t.getKategori().toLowerCase().contains(keyword.toLowerCase());
                return matchJenis && matchKeyword;
            })
            .toList();

        renderTransaksiList(filtered);
        updateSummary(filtered);
    }

    private void updateSummary(List<Transaksi> list) {
        double totalPemasukan = list.stream()
            .filter(t -> t.getJenis() == JenisTransaksi.PEMASUKAN)
            .mapToDouble(Transaksi::getJumlah).sum();
        double totalPengeluaran = list.stream()
            .filter(t -> t.getJenis() == JenisTransaksi.PENGELUARAN)
            .mapToDouble(Transaksi::getJumlah).sum();

        lblTotalPemasukan.setText(CurrencyFormatter.format(totalPemasukan));
        lblTotalPengeluaran.setText(CurrencyFormatter.format(totalPengeluaran));

        double max = Math.max(totalPemasukan, totalPengeluaran);
        if (max > 0 && progressPemasukan != null) {
            progressPemasukan.setProgress(totalPemasukan / max);
            progressPengeluaran.setProgress(totalPengeluaran / max);
        }
    }

    private void renderTopKategori(int userId) {
        if (topKategoriContainer == null) return;
        topKategoriContainer.getChildren().clear();
        Map<String, Double> byKat = service.getPengeluaranPerKategori(userId);

        byKat.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(3)
            .forEach(e -> {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);
                Label icon = new Label(getCategoryIcon(e.getKey()));
                icon.getStyleClass().add("transaksi-icon-sm");
                icon.setMinSize(32, 32);
                icon.setAlignment(Pos.CENTER);

                VBox info = new VBox(2);
                HBox.setHgrow(info, Priority.ALWAYS);
                Label name = new Label(e.getKey());
                name.getStyleClass().add("top-kat-name");

                // Label jumlah transaksi
                Label count = new Label("Kategori: " + e.getKey());
                count.getStyleClass().add("top-kat-count");
                info.getChildren().addAll(name, count);

                Label amount = new Label(CurrencyFormatter.format(e.getValue()));
                amount.getStyleClass().add("top-kat-amount");

                row.getChildren().addAll(icon, info, amount);
                topKategoriContainer.getChildren().add(row);
            });
    }

    @FXML
    private void handleTambah() {
        openFormTambah();
    }

    private void openFormTambah() {
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
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFormEdit(Transaksi transaksi) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/form-transaksi.fxml"));
            Parent root = loader.load();
            FormTransaksiController ctrl = loader.getController();
            ctrl.setMode(FormTransaksiController.Mode.EDIT, transaksi);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(SceneManager.getStage());
            dialog.setTitle("Edit Transaksi");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/styles/dark-theme.css").toExternalForm());
            dialog.setScene(scene);
            dialog.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void konfirmasiHapus(Transaksi t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Transaksi");
        alert.setContentText("Yakin ingin menghapus \"" + t.getJudul() + "\"?");
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/styles/dark-theme.css").toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.hapus(t.getId());
                loadData();
            } catch (Exception e) {
                Alert errAlert = new Alert(Alert.AlertType.ERROR);
                errAlert.setTitle("Error");
                errAlert.setHeaderText("Gagal menghapus transaksi");
                errAlert.setContentText(e.getMessage());
                errAlert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/styles/dark-theme.css").toExternalForm());
                errAlert.showAndWait();
            }
        }
    }

    @FXML private void handleToggleSidebar()   { sidebarToggle.toggle(sidebar); }
    @FXML private void handleDashboard()       { SceneManager.switchTo("dashboard"); }
    @FXML private void handleTransaksi()       { SceneManager.switchTo("transaksi"); }
    @FXML private void handleTarget()          { SceneManager.switchTo("target"); }
    @FXML private void handleLaporan()         { SceneManager.switchTo("laporan"); }
    @FXML private void handleProfil()         { SceneManager.switchTo("profil"); }

    @FXML private void handleCatatTransaksi()  { handleTambah(); }
    @FXML private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.switchTo("landing");
    }
}
