package com.simanja.controller;

import com.simanja.model.User;
import com.simanja.util.SceneManager;
import com.simanja.util.SessionManager;
import com.simanja.service.UserService;
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

        String username = txtUsername.getText().trim();
        String telepon = txtTelepon.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String jenisKelamin = cbJenisKelamin.getValue() != null ? cbJenisKelamin.getValue() : "";
        String tglLahir = txtTanggalLahir.getText().trim();

        // Cek apakah user ingin mengubah password
        String passwordLama = txtPasswordLama != null ? txtPasswordLama.getText().trim() : "";
        String passwordBaru = txtPasswordBaru != null ? txtPasswordBaru.getText().trim() : "";
        String passwordKonfirm = txtPasswordKonfirm != null ? txtPasswordKonfirm.getText().trim() : "";

        boolean gantiPassword = !passwordLama.isEmpty() || !passwordBaru.isEmpty() || !passwordKonfirm.isEmpty();

        if (gantiPassword) {
            // Validasi password
            if (passwordLama.isEmpty()) {
                showError("Password lama harus diisi.");
                return;
            }
            if (passwordBaru.isEmpty()) {
                showError("Password baru harus diisi.");
                return;
            }
            if (passwordBaru.length() < 6) {
                showError("Password baru minimal 6 karakter.");
                return;
            }
            if (!passwordBaru.equals(passwordKonfirm)) {
                showError("Konfirmasi password baru tidak cocok.");
                return;
            }

            // Ubah password dulu
            try {
                UserService userService = new UserService();
                userService.changePassword(passwordLama, passwordBaru, passwordKonfirm);
                showSuccess("Password berhasil diubah!");
            } catch (Exception e) {
                String errMsg = e.getMessage();
                if (errMsg.contains("Password lama tidak cocok")) {
                    showError("Password lama yang Anda masukkan salah.");
                } else if (errMsg.contains("Konfirmasi password baru tidak cocok")) {
                    showError("Konfirmasi password tidak cocok.");
                } else if (errMsg.contains("Password baru harus berbeda")) {
                    showError("Password baru harus berbeda dengan password lama.");
                } else {
                    showError("Gagal mengubah password: " + errMsg);
                }
                return;
            }

            // Clear password fields setelah berhasil
            txtPasswordLama.clear();
            txtPasswordBaru.clear();
            txtPasswordKonfirm.clear();
        }

        // Update profil (data pribadi)
        try {
            UserService userService = new UserService();
            User updatedUser = userService.updateProfile(namaLengkap, username, telepon, jenisKelamin, tglLahir, alamat, user.getProfileImagePath());
            
            // Karena email belum didukung update di endpoint backend saat ini, kita set manual jika perlu
            updatedUser.setEmail(email);
            
            // Timpa instance lama dengan yang baru
            SessionManager.getInstance().login(updatedUser, SessionManager.getInstance().getJwtToken());
            user = updatedUser;
        } catch (Exception e) {
            showError("Gagal menyimpan profil ke server: " + e.getMessage());
            return;
        }

        // Refresh view dan kembali ke mode view
        refreshView(user);
        
        if (gantiPassword) {
            showSuccess("Profil dan password berhasil diubah!");
        } else {
            showSuccess("Profil berhasil disimpan.");
        }
        
        // Delay sebelum kembali ke view mode
        javafx.application.Platform.runLater(() -> {
            try {
                Thread.sleep(1500); // Tampilkan pesan selama 1.5 detik
            } catch (InterruptedException ignored) {}
            showViewMode();
        });
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

                    // Simpan instan ke database
                    try {
                        UserService userService = new UserService();
                        User updatedUser = userService.updateProfile(
                            user.getNama(), user.getUsername(), user.getTelepon(), 
                            user.getJenisKelamin(), user.getTanggalLahir(), user.getAlamat(), 
                            user.getProfileImagePath()
                        );
                        updatedUser.setEmail(user.getEmail());
                        SessionManager.getInstance().login(updatedUser, SessionManager.getInstance().getJwtToken());
                    } catch (Exception ex) {
                        showError("Foto gagal disimpan ke database: " + ex.getMessage());
                    }

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
