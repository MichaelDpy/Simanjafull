package com.simanja.controller;

import com.simanja.model.Target;
import com.simanja.service.TargetService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class IsiCelenganController {

    @FXML private Label lblJudul;
    @FXML private TextField txtNominal;
    @FXML private ComboBox<String> cbSumber;
    @FXML private DatePicker dpTanggal;
    @FXML private Label lblProgressPct;
    @FXML private ProgressBar progressBar;
    @FXML private Label lblError;

    private final TargetService targetService = new TargetService();
    private Target target;

    @FXML
    public void initialize() {
        cbSumber.setItems(javafx.collections.FXCollections.observableArrayList(
            "💵 Tunai", "🏦 Bank BCA", "🏦 Bank Mandiri", "🏦 Bank Jago",
            "📱 GoPay", "📱 OVO", "📱 Dana", "📱 e-Wallet"));
        cbSumber.setValue("💵 Tunai");
        dpTanggal.setValue(LocalDate.now());
        if (lblError != null) {
            lblError.setVisible(false);
            lblError.setManaged(false);
        }
    }

    public void setTarget(Target target) {
        this.target = target;
        lblJudul.setText("Isi Celengan: " + target.getNama());
        double pct = target.getProgressPercent();
        lblProgressPct.setText(String.format("%.0f%%", pct));
        progressBar.setProgress(pct / 100.0);
    }

    @FXML
    private void handleSubmit() {
        if (lblError != null) {
            lblError.setText("");
            lblError.setVisible(false);
            lblError.setManaged(false);
        }
        if (target == null) return;

        String raw = txtNominal.getText().trim().replace(".", "").replace(",", "");
        if (raw.isEmpty()) {
            lblError.setText("Nominal harus diisi.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            lblError.setText("Nominal tidak valid.");
            return;
        }

        if (amount <= 0) {
            if (lblError != null) {
                lblError.setText("Nominal harus lebih dari 0.");
                lblError.setVisible(true);
                lblError.setManaged(true);
            }
            return;
        }

        targetService.isiCelengan(target.getId(), amount);
        close();
    }

    @FXML
    private void handleBatal() {
        close();
    }

    private void close() {
        Stage stage = (Stage) lblJudul.getScene().getWindow();
        stage.close();
    }
}
