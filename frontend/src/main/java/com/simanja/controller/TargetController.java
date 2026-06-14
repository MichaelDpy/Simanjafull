package com.simanja.controller;

import com.simanja.model.Target;
import com.simanja.model.User;
import com.simanja.service.TargetService;
import com.simanja.util.CurrencyFormatter;
import com.simanja.util.SceneManager;
import com.simanja.util.SessionManager;
import com.simanja.util.SidebarToggle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class TargetController {

    @FXML private Label lblTotalTabungan;
    @FXML private VBox targetListContainer;

    // --- Hamburger ---
    @FXML private VBox sidebar;

    private final TargetService targetService  = new TargetService();
    private final SidebarToggle sidebarToggle  = new SidebarToggle();
    private final DateTimeFormatter monthFmt   = DateTimeFormatter.ofPattern("MMMM yyyy");

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            SceneManager.switchTo("login");
            return;
        }
        refresh(user.getId());
    }

    private void refresh(int userId) {
        try {
            lblTotalTabungan.setText(CurrencyFormatter.format(targetService.getTotalTabungan(userId)));
            targetListContainer.getChildren().clear();
            for (Target target : targetService.getAllByUser(userId)) {
                targetListContainer.getChildren().add(buildTargetCard(target));
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat data target: " + e.getMessage());
            lblTotalTabungan.setText("Rp 0");
            targetListContainer.getChildren().clear();
            Label error = new Label("Gagal memuat data. Pastikan server backend berjalan.");
            error.setStyle("-fx-text-fill: #a0aabf; -fx-font-style: italic; -fx-padding: 24;");
            targetListContainer.getChildren().add(error);
        }
    }

    private HBox buildTargetCard(Target target) {
        boolean achieved = target.isAchieved();
        double pct = target.getProgressPercent();

        // ── Outer card: HBox (ikon | info+bar+amount tumbuh | tombol/label kanan)
        HBox card = new HBox(16);
        card.getStyleClass().add(achieved ? "target-card-achieved" : "target-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setAlignment(Pos.CENTER_LEFT);

        // Ikon
        Label icon = new Label(target.getIconEmoji());
        icon.getStyleClass().add("target-icon");
        icon.setMinSize(48, 48);
        icon.setAlignment(Pos.CENTER);

        // Kolom tengah: judul + subtitle + progress bar + amount (grows)
        VBox middle = new VBox(8);
        HBox.setHgrow(middle, Priority.ALWAYS);

        // Baris judul + pct
        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label(target.getNama());
        title.getStyleClass().add("target-card-title");
        HBox.setHgrow(title, Priority.ALWAYS);

        if (achieved) {
            Label badge = new Label("●TERCAPAI");
            badge.getStyleClass().add("target-badge-achieved");
            titleRow.getChildren().addAll(title, badge);
        } else {
            Label pctLabel = new Label(String.format("%.0f%%", pct));
            pctLabel.getStyleClass().add("progress-pct-label");
            titleRow.getChildren().addAll(title, pctLabel);
        }

        // Subtitle
        String subText;
        if (achieved) {
            subText = "Selesai: " + (target.getCompletedAt() != null
                ? target.getCompletedAt().format(monthFmt) : "");
        } else {
            subText = "Target: " + (target.getDeadline() != null
                ? target.getDeadline().format(monthFmt) : "");
        }
        Label subtitle = new Label(subText);
        subtitle.getStyleClass().add("target-card-subtitle");

        // Progress bar
        ProgressBar bar = new ProgressBar(pct / 100.0);
        bar.getStyleClass().add(achieved ? "target-progress-achieved" : "target-progress");
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setPrefHeight(8);

        // Amount label
        Label amount = new Label(
            CurrencyFormatter.format(target.getCurrentAmount()) + " / "
                + CurrencyFormatter.format(target.getTargetAmount()));
        amount.getStyleClass().add("target-amount-text");

        middle.getChildren().addAll(titleRow, subtitle, bar, amount);

        // Kolom kanan: tombol Isi Celengan atau label Tercapai (mentok kanan)
        // Dibungkus VBox agar bisa CENTER alignment vertikal
        VBox rightCol = new VBox();
        rightCol.setAlignment(Pos.CENTER);
        rightCol.setMinWidth(140);

        if (achieved) {
            Label done = new Label("Target Tercapai 🎉");
            done.getStyleClass().add("target-done-label");
            rightCol.getChildren().add(done);
        } else {
            Button btnIsi = new Button("🐷  Isi Celengan");
            btnIsi.getStyleClass().add("btn-isi-celengan");
            btnIsi.setOnAction(e -> openIsiCelengan(target));
            rightCol.getChildren().add(btnIsi);
        }

        card.getChildren().addAll(icon, middle, rightCol);
        return card;
    }

    private void openIsiCelengan(Target target) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/isi-celengan.fxml"));
            Parent root = loader.load();
            IsiCelenganController ctrl = loader.getController();
            ctrl.setTarget(target);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(SceneManager.getStage());
            dialog.setTitle("Isi Celengan");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/styles/dark-theme.css").toExternalForm());
            dialog.setScene(scene);
            dialog.showAndWait();

            int userId = SessionManager.getInstance().getCurrentUser().getId();
            refresh(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBuatTarget() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/buat-target.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(SceneManager.getStage());
            dialog.setTitle("Buat Target Baru");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/styles/dark-theme.css").toExternalForm());
            dialog.setScene(scene);
            dialog.showAndWait();

            int userId = SessionManager.getInstance().getCurrentUser().getId();
            refresh(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void handleDashboard()       { SceneManager.switchTo("dashboard"); }
    @FXML private void handleTransaksi()       { SceneManager.switchTo("transaksi"); }
    @FXML private void handleTarget()          { SceneManager.switchTo("target"); }
    @FXML private void handleLaporan()         { SceneManager.switchTo("laporan"); }
    @FXML private void handleCatatTransaksi()  { SceneManager.switchTo("transaksi"); }
    @FXML private void handleProfil()         { SceneManager.switchTo("profil"); }

    @FXML private void handleToggleSidebar()   { sidebarToggle.toggle(sidebar); }
    @FXML private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.switchTo("landing");
    }
}
