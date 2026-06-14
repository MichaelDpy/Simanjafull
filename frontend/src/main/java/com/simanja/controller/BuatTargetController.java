package com.simanja.controller;

import com.simanja.service.TargetService;
import com.simanja.util.CurrencyFormatter;
import com.simanja.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class BuatTargetController {

    @FXML private TextField txtNama;
    @FXML private Label lblIconSelected;
    @FXML private TextField txtNominal;
    @FXML private DatePicker dpDeadline;
    @FXML private Label lblInsight;
    @FXML private Label lblError;
    @FXML private Button btnIconLaptop;
    @FXML private Button btnIconCar;
    @FXML private Button btnIconBeach;
    @FXML private Button btnIconHouse;
    @FXML private Button btnIconMoney;
    @FXML private Button btnIconTarget;

    private final TargetService targetService = new TargetService();
    private String selectedIcon = "🎯";

    @FXML
    public void initialize() {
        dpDeadline.setValue(LocalDate.now().plusMonths(6));
        txtNominal.textProperty().addListener((o, a, b) -> updateInsight());
        dpDeadline.valueProperty().addListener((o, a, b) -> updateInsight());
        if (lblError != null) {
            lblError.setVisible(false);
            lblError.setManaged(false);
        }
        updateInsight();
    }

    @FXML
    private void handlePickIcon(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        selectedIcon = btn.getText();
        lblIconSelected.setText(selectedIcon);
    }

    private void updateInsight() {
        String raw = txtNominal.getText().trim().replace(".", "").replace(",", "");
        if (raw.isEmpty() || dpDeadline.getValue() == null) {
            lblInsight.setText("Masukkan nominal dan tenggat waktu untuk melihat estimasi tabungan bulanan.");
            return;
        }
        try {
            double amount = Double.parseDouble(raw);
            double monthly = targetService.estimasiTabunganBulanan(amount, dpDeadline.getValue());
            lblInsight.setText("Kamu perlu menabung sekitar "
                + CurrencyFormatter.format(monthly) + "/bulan untuk mencapai target ini.");
        } catch (NumberFormatException e) {
            lblInsight.setText("Nominal target tidak valid.");
        }
    }

    @FXML
    private void handleSubmit() {
        if (lblError != null) {
            lblError.setText("");
            lblError.setVisible(false);
            lblError.setManaged(false);
        }
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            if (lblError != null) {
                lblError.setText("Nama target harus diisi.");
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
            return;
        }

        String raw = txtNominal.getText().trim().replace(".", "").replace(",", "");
        if (raw.isEmpty()) {
            if (lblError != null) {
                lblError.setText("Nominal target harus diisi.");
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            if (lblError != null) {
                lblError.setText("Nominal target tidak valid.");
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
            return;
        }

        if (amount <= 0) {
            if (lblError != null) {
                lblError.setText("Nominal target harus lebih dari 0.");
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
            return;
        }

        if (dpDeadline.getValue() == null) {
            if (lblError != null) {
                lblError.setText("Tenggat waktu harus diisi.");
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
            return;
        }

        try {
            int userId = SessionManager.getInstance().getCurrentUser().getId();
            targetService.buatTarget(nama, selectedIcon, amount, dpDeadline.getValue(), userId);
            close();
        } catch (Exception e) {
            if (lblError != null) {
                lblError.setText("Gagal membuat target: " + e.getMessage());
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
        }
    }

    @FXML
    private void handleBatal() {
        close();
    }

    private void close() {
        Stage stage = (Stage) txtNama.getScene().getWindow();
        stage.close();
    }
}
