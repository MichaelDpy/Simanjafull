package com.simanja.controller;

import com.simanja.model.Transaksi;
import com.simanja.model.Transaksi.JenisTransaksi;
import com.simanja.service.TransaksiService;
import com.simanja.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller untuk Form Tambah/Edit Transaksi
 * Demonstrasi: Validasi input, Polymorphism (mode tambah/edit)
 */
public class FormTransaksiController {

    public enum Mode { TAMBAH, EDIT }

    @FXML private Label lblJudul;
    @FXML private Button btnJenisPengeluaran;
    @FXML private Button btnJenisPemasukan;
    @FXML private TextField txtJumlah;
    @FXML private ComboBox<String> cbKategori;
    @FXML private ComboBox<String> cbSumberDana;
    @FXML private DatePicker dpTanggal;
    @FXML private TextArea txtKeterangan;
    @FXML private Button btnSimpan;
    @FXML private Button btnBatal;
    @FXML private Label lblError;

    private Mode mode;
    private Transaksi transaksiEdit;
    private JenisTransaksi jenisAktif = JenisTransaksi.PENGELUARAN;
    private final TransaksiService service = new TransaksiService();

    @FXML
    public void initialize() {
        cbSumberDana.setItems(FXCollections.observableArrayList(
            "💵 Tunai", "🏦 Bank BCA", "🏦 Bank Mandiri", "🏦 Bank Jago",
            "📱 GoPay", "📱 OVO", "📱 Dana", "📱 e-Wallet"
        ));
        cbSumberDana.setValue("💵 Tunai");

        dpTanggal.setValue(LocalDate.now());
        lblError.setVisible(false);
        lblError.setManaged(false);

        setJenisPengeluaran();
    }

    public void setMode(Mode mode, Transaksi transaksi) {
        this.mode = mode;
        this.transaksiEdit = transaksi;

        if (mode == Mode.EDIT && transaksi != null) {
            lblJudul.setText("Edit Transaksi");
            btnSimpan.setText("Update Transaksi");
            txtJumlah.setText(String.valueOf((long) transaksi.getJumlah()));
            if (transaksi.getJenis() == JenisTransaksi.PEMASUKAN) {
                setJenisPemasukan();
            } else {
                setJenisPengeluaran();
            }
            cbKategori.setValue(transaksi.getKategori());
            dpTanggal.setValue(transaksi.getTanggal());
            txtKeterangan.setText(transaksi.getKeterangan());
        } else {
            lblJudul.setText("Catat Transaksi Baru");
            btnSimpan.setText("Simpan Transaksi");
        }
    }

    @FXML
    private void handleTogglePengeluaran() {
        setJenisPengeluaran();
    }

    @FXML
    private void handleTogglePemasukan() {
        setJenisPemasukan();
    }

    private void setJenisPengeluaran() {
        jenisAktif = JenisTransaksi.PENGELUARAN;
        btnJenisPengeluaran.getStyleClass().setAll("toggle-btn-active-red");
        btnJenisPemasukan.getStyleClass().setAll("toggle-btn-inactive");
        cbKategori.setItems(FXCollections.observableArrayList(
            "🍔 Makanan", "🚗 Transportasi", "🛒 Belanja", "📋 Tagihan",
            "🎮 Hiburan", "🏥 Kesehatan", "📚 Pendidikan", "🏠 Rumah", "💡 Lainnya"
        ));
        cbKategori.setValue("🍔 Makanan");
    }

    private void setJenisPemasukan() {
        jenisAktif = JenisTransaksi.PEMASUKAN;
        btnJenisPemasukan.getStyleClass().setAll("toggle-btn-active-green");
        btnJenisPengeluaran.getStyleClass().setAll("toggle-btn-inactive");
        cbKategori.setItems(FXCollections.observableArrayList(
            "💰 Gaji", "💼 Freelance", "🎁 Bonus", "📈 Investasi",
            "🏪 Bisnis", "💵 Pendapatan Lain"
        ));
        cbKategori.setValue("💰 Gaji");
    }

    @FXML
    private void handleSimpan() {
        hideError();

        String jumlahStr  = txtJumlah.getText().trim().replace(".", "").replace(",", "");
        String kategori   = cbKategori.getValue() != null
            ? cbKategori.getValue().replaceAll("^[^a-zA-Z]*", "").trim() : "";
        LocalDate tanggal = dpTanggal.getValue();
        String keterangan = txtKeterangan.getText() != null ? txtKeterangan.getText().trim() : "";

        double jumlah;
        try {
            jumlah = Double.parseDouble(jumlahStr);
        } catch (NumberFormatException e) {
            showError("Jumlah harus berupa angka.");
            return;
        }

        if (jumlah <= 0) {
            showError("Jumlah harus lebih dari 0.");
            return;
        }

        if (tanggal == null) {
            showError("Tanggal harus diisi.");
            return;
        }

        // Gunakan "Transaksi" sebagai judul default jika tidak ada field judul
        String judul = jenisAktif == JenisTransaksi.PENGELUARAN
            ? kategori : "Pemasukan - " + kategori;

        try {
            if (mode == Mode.TAMBAH || mode == null) {
                int userId = SessionManager.getInstance().getCurrentUser().getId();
                service.tambah(judul, jumlah, jenisAktif, kategori, tanggal, keterangan, userId);
            } else if (mode == Mode.EDIT && transaksiEdit != null) {
                service.update(transaksiEdit.getId(), judul, jumlah, jenisAktif,
                               kategori, tanggal, keterangan);
            }
            closeDialog();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleBatal() {
        closeDialog();
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void hideError() {
        lblError.setVisible(false);
        lblError.setManaged(false);
    }

    private void closeDialog() {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}
