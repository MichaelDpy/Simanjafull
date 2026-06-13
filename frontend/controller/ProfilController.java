package com.simanja.controller;

import com.simanja.model.User;
import com.simanja.util.SceneManager;
import com.simanja.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller untuk halaman Profil & Pengaturan
 * Dua mode: VIEW (tampil info) dan EDIT (form editable)
 */
public class ProfilController {

    // ── Avatar + header ──
    @FXML private Label     lblAvatar;
    @FXML private ImageView imgAvatar;
    @FXML private Label     lblNamaDisplay;
    @FXML private Label     lblEmailDisplay;
    @FXML private Label     lblMemberSince;
    @FXML private Label     lblVerifikasi;

    // ── View mode labels ──
    @FXML private Label viewUsername;
    @FXML private Label viewNama;
    @FXML private Label viewJenisKelamin;
    @FXML private Label viewTanggalLahir;
    @FXML private Label viewAlamat;
    @FXML private Label viewEmail;
    @FXML private Label viewTelepon;

    // ── Kartu view vs edit ──
    @FXML private VBox viewCard;
    @FXML private VBox editCard;

    // ── Edit mode fields ──
    @FXML private TextField     txtUsername;
    @FXML private TextField     txtNamaLengkap;
    @FXML private ComboBox<String> cbJenisKelamin;
    @FXML private TextField     txtTanggalLahir;
    @FXML private TextField     txtEmail;
    @FXML private TextField     txtTelepon;
    @FXML private TextArea      txtAlamat;
    @FXML private PasswordField txtPasswordLama;
    @FXML private PasswordField txtPasswordBaru;
    @FXML private PasswordField txtPasswordKonfirm;

    // ── Pesan ──
    @FXML private Label lblProfilError;
    @FXML private Label lblProfilSuccess;

    // ──────────────────────────────────────────────
    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            SceneManager.switchTo("login");
            return;
        }

        // Isi ComboBox jenis kelamin
        cbJenisKelamin.setItems(FXCollections.observableArrayList(
            "Laki-laki", "Perempuan", "Lainnya"));

        // Sembunyikan pesan
        hideMessages();

        // Render view mode
        refreshView(user);

        // Mulai di view mode
        showViewMode();
    }

    /** Memuat dan menampilkan foto profil ke ImageView, atau fallback ke inisial */
    private void applyProfileImage(User user) {
        String path = user.getProfileImagePath();
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    Image img = new Image(file.toURI().toString(), 88, 88, false, true);
                    imgAvatar.setImage(img);
                    imgAvatar.setVisible(true);
                    imgAvatar.setManaged(true);
                    lblAvatar.setVisible(false);
                    lblAvatar.setManaged(false);
                    return;
                } catch (Exception ignored) {}
            }
        }
        // Fallback: tampilkan inisial
        imgAvatar.setVisible(false);
        imgAvatar.setManaged(false);
        lblAvatar.setVisible(true);
        lblAvatar.setManaged(true);
    }

    /** Isi semua label view-mode dari objek user */
    private void refreshView(User user) {
        // Avatar inisial
        String initial = user.getNama() != null && !user.getNama().isEmpty()
            ? String.valueOf(user.getNama().charAt(0)).toUpperCase() : "U";
        lblAvatar.setText(initial);

        // Terapkan foto atau fallback inisial
        applyProfileImage(user);

        lblNamaDisplay.setText(orDash(user.getNama()));
        lblEmailDisplay.setText(orDash(user.getEmail()));

        String since = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("id-ID")));
        lblMemberSince.setText("Pengguna Setia sejak " + since);

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("d MMMM yyyy",
            Locale.forLanguageTag("id-ID")));
        lblVerifikasi.setText("Akun Anda telah terverifikasi penuh sejak " + todayStr + ".");

        // Kolom identitas
        viewUsername.setText(orDash(user.getUsername()));
        viewNama.setText(orDash(user.getNama()));
        viewJenisKelamin.setText(orDash(user.getJenisKelamin()));
        viewTanggalLahir.setText(orDash(user.getTanggalLahir()));
        viewAlamat.setText(orDash(user.getAlamat()));

        // Kolom kontak
        viewEmail.setText(orDash(user.getEmail()));
        viewTelepon.setText(orDash(user.getTelepon()));
    }

    /** Isi form edit dari objek user */
    private void fillEditForm(User user) {
        txtUsername.setText(orEmpty(user.getUsername()));
        txtNamaLengkap.setText(orEmpty(user.getNama()));
        cbJenisKelamin.setValue(user.getJenisKelamin() != null
            && !user.getJenisKelamin().isEmpty() ? user.getJenisKelamin() : null);
        txtTanggalLahir.setText(orEmpty(user.getTanggalLahir()));
        txtEmail.setText(orEmpty(user.getEmail()));
        txtTelepon.setText(orEmpty(user.getTelepon()));
        txtAlamat.setText(orEmpty(user.getAlamat()));

        // Bersihkan field password setiap kali buka edit
        if (txtPasswordLama   != null) txtPasswordLama.clear();
        if (txtPasswordBaru   != null) txtPasswordBaru.clear();
        if (txtPasswordKonfirm != null) txtPasswordKonfirm.clear();
    }

    // ── Toggle antara view dan edit ──

    @FXML
    private void handleEditMode() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;
        fillEditForm(user);
        hideMessages();
        showEditMode();
    }

    @FXML
    private void handleCancelEdit() {
        hideMessages();
        showViewMode();
    }

    private void showViewMode() {
        viewCard.setVisible(true);
        viewCard.setManaged(true);
        editCard.setVisible(false);
        editCard.setManaged(false);
    }

    private void showEditMode() {
        viewCard.setVisible(false);
        viewCard.setManaged(false);
        editCard.setVisible(true);
        editCard.setManaged(true);
    }

    // ── Simpan perubahan ──

    @FXML
    private void handleSimpan() {
        hideMessages();
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        String namaLengkap = txtNamaLengkap.getText().trim();
        if (namaLengkap.isEmpty()) {
            showError("Nama lengkap tidak boleh kosong.");
            return;
        }
        String email = txtEmail.getText().trim();
        if (email.isEmpty() || !email.contains("@")) {
            showError("Format email tidak valid.");
            return;
        }

        // Update semua field
        user.setNama(namaLengkap);
        user.setUsername(txtUsername.getText().trim());
        user.setEmail(email);
        user.setTelepon(txtTelepon.getText().trim());
        user.setAlamat(txtAlamat.getText().trim());
        if (cbJenisKelamin.getValue() != null) {
            user.setJenisKelamin(cbJenisKelamin.getValue());
        }
        user.setTanggalLahir(txtTanggalLahir.getText().trim());

        // Ubah password jika diisi
        String passBaru = txtPasswordBaru.getText();
        if (!passBaru.isEmpty()) {
            String passLama    = txtPasswordLama.getText();
            String passKonfirm = txtPasswordKonfirm.getText();

            if (passLama.isEmpty()) {
                showError("Masukkan kata sandi lama untuk mengubah password.");
                return;
            }
            if (!passLama.equals(user.getPassword())) {
                showError("Kata sandi lama tidak cocok.");
                return;
            }
            if (passBaru.length() < 6) {
                showError("Kata sandi baru minimal 6 karakter.");
                return;
            }
            if (!passBaru.equals(passKonfirm)) {
                showError("Konfirmasi kata sandi baru tidak cocok.");
                return;
            }
            user.setPassword(passBaru);
        }

        // Refresh view dan kembali ke mode view
        refreshView(user);
        showSuccess("Perubahan berhasil disimpan.");
        showViewMode();
    }

    // ── Ubah Foto Profil ──

    @FXML
    private void handleUbahFoto() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("File Gambar", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp")
        );

        // Buka dialog dari stage manapun yang sedang aktif
        Stage stage = (Stage) lblNamaDisplay.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image img = new Image(selectedFile.toURI().toString(), 88, 88, false, true);
                if (!img.isError()) {
                    // Simpan path ke model user
                    user.setProfileImagePath(selectedFile.getAbsolutePath());

                    // Tampilkan foto baru
                    imgAvatar.setImage(img);
                    imgAvatar.setVisible(true);
                    imgAvatar.setManaged(true);
                    lblAvatar.setVisible(false);
                    lblAvatar.setManaged(false);

                    showSuccess("Foto profil berhasil diperbarui.");
                } else {
                    showError("File gambar tidak valid atau tidak dapat dibaca.");
                }
            } catch (Exception e) {
                showError("Gagal memuat foto: " + e.getMessage());
            }
        }
    }

    // ── Navigasi ──
    @FXML
    private void handleBack() {
        SceneManager.switchTo("dashboard");
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.switchTo("landing");
    }

    // ── Helper ──
    private String orDash(String s)  { return (s != null && !s.isEmpty()) ? s : "—"; }
    private String orEmpty(String s) { return s != null ? s : ""; }

    private void showError(String msg) {
        lblProfilError.setText(msg);
        lblProfilError.setVisible(true);
        lblProfilError.setManaged(true);
    }

    private void showSuccess(String msg) {
        lblProfilSuccess.setText(msg);
        lblProfilSuccess.setVisible(true);
        lblProfilSuccess.setManaged(true);
    }

    private void hideMessages() {
        lblProfilError.setVisible(false);
        lblProfilError.setManaged(false);
        lblProfilSuccess.setVisible(false);
        lblProfilSuccess.setManaged(false);
    }
}
