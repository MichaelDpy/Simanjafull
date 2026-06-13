# SiManja — Sistem Manajemen Keuangan (JavaFX Frontend)

Aplikasi manajemen keuangan berbasis desktop dengan JavaFX, dark theme.

## Akun Demo (Data Dummy)
| Nama           | Email               | Password   | Role  |
|----------------|---------------------|------------|-------|
| Admin SiManja  | admin@simanja.com   | admin123   | ADMIN |
| Budi Santoso   | budi@simanja.com    | budi123    | USER  |
| Siti Rahayu    | siti@simanja.com    | siti123    | USER  |

## Cara Menjalankan
```bash
mvn javafx:run
```

## Struktur Proyek
```
src/main/java/com/simanja/
├── MainApp.java                    # Entry point
├── model/
│   ├── BaseEntity.java             # Abstract class (Inheritance)
│   ├── Kategorisasi.java           # Interface (Abstraction)
│   ├── User.java                   # Model user (Encapsulation)
│   └── Transaksi.java              # Model transaksi (Encapsulation + Abstraction)
├── service/
│   ├── AuthService.java            # Logika autentikasi + validasi
│   └── TransaksiService.java       # Logika bisnis transaksi + validasi
├── controller/
│   ├── LandingController.java      # Landing page
│   ├── LoginController.java        # Login (Security/Auth)
│   ├── RegisterController.java     # Register
│   ├── DashboardController.java    # Dashboard + charts
│   ├── TransaksiController.java    # CRUD transaksi
│   ├── FormTransaksiController.java# Form tambah/edit (Polymorphism)
│   └── LaporanController.java      # Laporan + bar chart
└── util/
    ├── SceneManager.java           # Navigasi antar scene
    ├── SessionManager.java         # Singleton session (Encapsulation)
    └── CurrencyFormatter.java      # Format Rupiah

src/main/resources/
├── fxml/                           # File tampilan UI
│   ├── landing.fxml
│   ├── login.fxml
│   ├── register.fxml
│   ├── dashboard.fxml
│   ├── transaksi.fxml
│   ├── form-transaksi.fxml
│   └── laporan.fxml
└── styles/
    └── dark-theme.css              # Dark theme (navy + hijau)
```

## Pemenuhan Kriteria PBO

| Kriteria | Implementasi |
|----------|-------------|
| JavaFX UI | Semua screen menggunakan JavaFX + FXML |
| Arsitektur MVC | Controller ↔ Service ↔ Model |
| Service Layer | `AuthService`, `TransaksiService` |
| Validasi | Input divalidasi di Service layer |
| Security | `AuthService.login()`, `SessionManager` |
| Encapsulation | Semua field private + getter/setter |
| Inheritance | `BaseEntity` sebagai abstract class |
| Polymorphism | `FormTransaksiController.Mode` enum + lambda stream |
| Abstraction | Interface `Kategorisasi`, abstract class `BaseEntity` |
