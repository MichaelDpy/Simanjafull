# SiManja Backend — Spring Boot REST API

<div align="center">

**REST API Backend untuk Sistem Manajemen Keuangan (SiManja)**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![H2 Database](https://img.shields.io/badge/H2-2.2.224-blue.svg)](https://www.h2database.com/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

*Project UAS Lab Pemrograman Berorientasi Objek*

</div>

---

## 📋 Daftar Isi

- [Tech Stack](#-tech-stack)
- [Fitur Utama](#-fitur-utama)
- [Cara Menjalankan](#-cara-menjalankan)
- [Akun Demo](#-akun-demo)
- [Endpoint API](#-endpoint-api)
- [Database H2](#-database-h2)
- [Keamanan](#-keamanan)
- [Validasi](#-validasi)
- [Arsitektur](#-arsitektur)
- [Pemenuhan Kriteria PBO](#-pemenuhan-kriteria-pbo)
- [Testing](#-testing)
- [Troubleshooting](#-troubleshooting)
- [File Penting](#-file-penting)
- [Changelog](#-changelog)

---

## 🛠 Tech Stack

| Komponen | Teknologi | Versi | Fungsi |
|----------|-----------|-------|--------|
| Framework | Spring Boot | 3.2.5 | Core application framework |
| REST API | Spring Web | 3.2.5 | MVC Controller & REST endpoints |
| ORM | Spring Data JPA + Hibernate | 3.2.5 | Database mapping & queries |
| Database | H2 Database | 2.2.224 | In-memory database (dev/testing) |
| Security | Spring Security + JWT | 3.2.5 | Authentication & authorization |
| JWT Library | JJWT | 0.11.5 | Token generation & validation |
| Validasi | Bean Validation | 3.0.2 | Input validation annotations |
| Password | BCrypt | - | Password hashing algorithm |
| Language | Java | 17 | Programming language |
| Build Tool | Maven | 3.6+ | Dependency management & build |

---

## 🎯 Fitur Utama

### **1. Authentication & Authorization**
- 🔐 JWT-based authentication (stateless)
- 👤 Role-based access control (ADMIN/USER)
- 🔑 BCrypt password hashing
- 📝 User registration & login
- 🔄 Change password feature

### **2. Manajemen Transaksi**
- ➕ Tambah transaksi (Pemasukan/Pengeluaran)
- ✏️ Edit & hapus transaksi
- 🔍 Search transaksi by keyword
- 📋 List semua transaksi user
- 📊 Ringkasan keuangan (saldo, total pemasukan/pengeluaran)
- 📈 Laporan per bulan
- ⏱️ 5 transaksi terbaru

### **3. Target Tabungan (Celengan)**
- 🎯 Buat target tabungan dengan deadline
- 💰 Isi celengan (deposit funds)
- 📊 Progress tracking (percentage)
- ✅ Auto-achievement detection
- 🗑️ Edit & hapus target
- 💵 Total tabungan user

### **4. User Profile Management**
- 👤 View profil lengkap
- ✏️ Update profil (nama, username, telepon, alamat, dll)
- 🔐 Change password with validation
- 🖼️ Profile image path support

### **5. Admin Features**
- 👥 View all users
- 🔍 Get user by ID
- 📊 Full system access

---

## 🚀 Cara Menjalankan

### **Persyaratan Sistem**

Pastikan sudah terinstall:
- ✅ **Java 17** atau higher → [Download](https://adoptium.net/)
- ✅ **Apache Maven 3.6+** → [Download](https://maven.apache.org/download.cgi)

Verifikasi instalasi:
```bash
java -version     # Harus Java 17+
mvn -version      # Harus Maven 3.6+
```

---

### **Metode 1: Quick Start (Recommended)** ⭐

Cara tercepat untuk menjalankan aplikasi:

```bash
cd backend
QUICK_START.bat
```

**Script akan otomatis:**
1. ✅ Check Maven installation
2. ✅ Compile project (jika belum)
3. ✅ Start Spring Boot application
4. ✅ Menampilkan server info & demo accounts

---

### **Metode 2: Fix Database Issues**

Jika ada masalah koneksi database atau compilation error:

```bash
cd backend
FIX_DATABASE.bat
```

**Script akan otomatis:**
1. Clean project (`mvn clean`)
2. Remove old data folder (jika ada)
3. Clear Maven cache untuk H2
4. Download fresh dependencies
5. Compile project
6. Start application

---

### **Metode 3: Manual dengan Maven**

```bash
cd backend

# Clean & start
mvn clean spring-boot:run

# Atau dengan package
mvn clean package -DskipTests
mvn spring-boot:run
```

---

### **Metode 4: Dari IDE**

#### **IntelliJ IDEA:**
1. `File` → `Open` → Pilih folder `backend`
2. Wait for Maven import
3. Right-click `SimanjaBackendApplication.java`
4. `Run 'SimanjaBackendApplication'`

#### **Eclipse:**
1. `File` → `Import` → `Existing Maven Project`
2. Select folder `backend`
3. Right-click project → `Run As` → `Spring Boot App`

---

### **Verifikasi Server Berjalan**

Setelah start, Anda akan lihat output:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.5)

...
INFO  - Started SimanjaBackendApplication in 3.456 seconds
```

**Server Information:**
- 🌐 **Base URL:** `http://localhost:8080`
- 🗄️ **H2 Console:** `http://localhost:8080/h2-console`
- 📡 **API Base:** `http://localhost:8080/api`

---

## 👤 Akun Demo

Data demo akan **otomatis dimuat** saat aplikasi start melalui `DataInitializer.java`.

| Nama | Email | Password | Role | Data Demo |
|------|-------|----------|------|-----------|
| **Admin SiManja** | `admin@simanja.com` | `admin123` | ADMIN | Full access, no transaction data |
| **Budi Santoso** | `budi@simanja.com` | `budi123` | USER | ✅ 9 transaksi + 4 target tabungan |
| **Siti Rahayu** | `siti@simanja.com` | `siti123` | USER | Empty data (fresh account) |

**Demo Data untuk Budi:**
- **9 Transaksi:** Gaji, Freelance, Belanja, Listrik, Makan, Transport, Investasi, Bonus, Netflix
- **4 Target:** Liburan Jepang, Dana Darurat (achieved), Macbook Pro, DP Rumah

**Penggunaan:**
```bash
# Login sebagai Budi untuk testing dengan data lengkap
POST /api/auth/login
{
  "email": "budi@simanja.com",
  "password": "budi123"
}

# Login sebagai Admin untuk testing admin features
POST /api/auth/login
{
  "email": "admin@simanja.com",
  "password": "admin123"
}
```

---

## 💾 H2 Console

H2 Console dapat diakses untuk melihat database secara langsung.

**URL:** `http://localhost:8080/h2-console`

**Login Settings:**

| Setting | Value |
|---------|-------|
| JDBC URL | `jdbc:h2:mem:simanja_db` |
| Driver Class | `org.h2.Driver` |
| Username | `sa` |
| Password | *(kosong)* |

**Tables:**
- `USERS` - Data user & credentials
- `TRANSAKSI` - Data transaksi keuangan  
- `TARGET` - Data target tabungan/celengan

**⚠️ Note:** Database adalah **in-memory**, data akan hilang saat aplikasi dimatikan dan akan di-seed ulang saat start. Cocok untuk development & testing.

---

## 🌐 Endpoint API

### **Ringkasan Endpoints**

| Kategori | Jumlah | Public | Protected | Admin Only |
|----------|--------|--------|-----------|------------|
| **Auth** | 2 | 2 | 0 | 0 |
| **User** | 5 | 0 | 3 | 2 |
| **Transaksi** | 9 | 0 | 9 | 0 |
| **Target** | 7 | 0 | 7 | 0 |
| **Total** | **23** | **2** | **19** | **2** |

---

### **1. Authentication (Public Endpoints)**

Endpoint yang dapat diakses tanpa JWT token.

#### **POST** `/api/auth/login`
Login user dan mendapatkan JWT token.

**Request Body:**
```json
{
  "email": "budi@simanja.com",
  "password": "budi123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJidWRp...",
  "tokenType": "Bearer",
  "userId": 2,
  "nama": "Budi Santoso",
  "email": "budi@simanja.com",
  "role": "USER"
}
```

**Validation:**
- ✅ Email: required, valid format
- ✅ Password: required

---

#### **POST** `/api/auth/register`
Register user baru.

**Request Body:**
```json
{
  "nama": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "konfirmPassword": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "userId": 4,
  "nama": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

**Validation:**
- ✅ Nama: required
- ✅ Email: required, valid format, unique
- ✅ Password: required, min 6 characters
- ✅ Konfirm Password: required, must match password

---

### **2. User Management (Protected Endpoints)**

Semua endpoint memerlukan JWT token di header:
```
Authorization: Bearer <your_jwt_token>
```

| Method | Endpoint | Role | Deskripsi |
|--------|----------|------|-----------|
| GET | `/api/users/me` | USER/ADMIN | Get profil user login |
| PUT | `/api/users/me` | USER/ADMIN | Update profil |
| PUT | `/api/users/change-password` | USER/ADMIN | Ubah password ⭐ |
| GET | `/api/users` | ADMIN | Get semua user |
| GET | `/api/users/{id}` | ADMIN | Get user by ID |

#### **GET** `/api/users/me`
Mendapatkan profil user yang sedang login.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "id": 2,
  "nama": "Budi Santoso",
  "email": "budi@simanja.com",
  "username": "budisantoso",
  "telepon": "0812-3456-7890",
  "jenisKelamin": "Laki-laki",
  "tanggalLahir": "15 Juni 1995",
  "alamat": "Jl. Midnight Ledger No. 88, Jakarta Selatan",
  "role": "USER"
}
```

---

#### **PUT** `/api/users/me`
Update profil user.

**Request Body:**
```json
{
  "nama": "Budi Santoso Updated",
  "username": "budisantoso",
  "telepon": "0812-9999-8888",
  "jenisKelamin": "Laki-laki",
  "tanggalLahir": "15 Juni 1995",
  "alamat": "Alamat baru",
  "profileImagePath": "/path/to/image.jpg"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "nama": "Budi Santoso Updated",
  ...
}
```

---

#### **PUT** `/api/users/change-password` ⭐ **NEW**
Ubah password user dengan validasi old password.

**Request Body:**
```json
{
  "oldPassword": "budi123",
  "newPassword": "budibaru123",
  "confirmPassword": "budibaru123"
}
```

**Response (200 OK):**
```json
{
  "message": "Password berhasil diubah"
}
```

**Validation:**
- ✅ Old Password: required, must match current password (BCrypt)
- ✅ New Password: required, min 6 characters, must differ from old password
- ✅ Confirm Password: required, must match new password

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2026-06-14T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Password lama tidak cocok"
}
```

---

### **3. Transaction Management (Protected Endpoints)**

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/transaksi` | Get semua transaksi user |
| GET | `/api/transaksi?keyword={keyword}` | Search transaksi |
| GET | `/api/transaksi/recent` | Get 5 transaksi terbaru |
| GET | `/api/transaksi/summary` | Get ringkasan keuangan |
| GET | `/api/transaksi/laporan` | Get data laporan per bulan |
| GET | `/api/transaksi/{id}` | Get detail transaksi |
| POST | `/api/transaksi` | Tambah transaksi baru |
| PUT | `/api/transaksi/{id}` | Update transaksi |
| DELETE | `/api/transaksi/{id}` | Hapus transaksi |

#### **POST** `/api/transaksi`
Tambah transaksi baru.

**Request Body:**
```json
{
  "judul": "Gaji Bulanan",
  "jumlah": 5000000,
  "jenis": "PEMASUKAN",
  "kategori": "Gaji",
  "tanggal": "2026-06-01",
  "keterangan": "Gaji bulan Juni"
}
```

**Response (200 OK):**
```json
{
  "id": 10,
  "judul": "Gaji Bulanan",
  "jumlah": 5000000.0,
  "jenis": "PEMASUKAN",
  "kategori": "Gaji",
  "tanggal": "2026-06-01",
  "keterangan": "Gaji bulan Juni"
}
```

**Validation:**
- ✅ Judul: required
- ✅ Jumlah: required, must be positive
- ✅ Jenis: required, must be "PEMASUKAN" or "PENGELUARAN"
- ✅ Kategori: required
- ✅ Tanggal: required, cannot be in the future

---

#### **GET** `/api/transaksi/summary`
Get ringkasan keuangan.

**Response (200 OK):**
```json
{
  "totalPemasukan": 14000000.0,
  "totalPengeluaran": 3139000.0,
  "saldo": 10861000.0,
  "totalTabungan": 122000000.0,
  "jumlahTransaksi": 9,
  "jumlahTarget": 4
}
```

---

### **4. Target/Savings Management (Protected Endpoints)**

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/target` | Get semua target user |
| GET | `/api/target/{id}` | Get detail target |
| GET | `/api/target/total-tabungan` | Get total tabungan |
| POST | `/api/target` | Buat target baru |
| PUT | `/api/target/{id}` | Update target |
| DELETE | `/api/target/{id}` | Hapus target |
| POST | `/api/target/{id}/isi` | Isi celengan |

#### **POST** `/api/target`
Buat target tabungan baru.

**Request Body:**
```json
{
  "nama": "Liburan Bali",
  "iconEmoji": "🏖️",
  "targetAmount": 10000000,
  "deadline": "2026-12-31"
}
```

**Response (200 OK):**
```json
{
  "id": 5,
  "nama": "Liburan Bali",
  "iconEmoji": "🏖️",
  "targetAmount": 10000000.0,
  "currentAmount": 0.0,
  "deadline": "2026-12-31",
  "achieved": false,
  "progressPercent": 0.0
}
```

**Validation:**
- ✅ Nama: required
- ✅ Target Amount: required, must be positive
- ✅ Deadline: required, must be in the future

---

#### **POST** `/api/target/{id}/isi`
Isi celengan (deposit funds).

**Request Body:**
```json
{
  "amount": 500000
}
```

**Response (200 OK):**
```json
{
  "id": 5,
  "nama": "Liburan Bali",
  "currentAmount": 500000.0,
  "targetAmount": 10000000.0,
  "progressPercent": 5.0,
  "achieved": false
}
```

**Note:** Jika `currentAmount >= targetAmount`, status `achieved` otomatis menjadi `true`.

---

## 🔐 Keamanan

Backend SiManja menerapkan multi-layer security untuk melindungi data dan API.

### **1. Authentication & Authorization**

#### **JWT (JSON Web Token)**
- ✅ Stateless authentication (no session)
- ✅ Token expiration: **24 jam**
- ✅ Token structure: `Bearer <token>`
- ✅ Token payload: `userId`, `email`, `role`

**Contoh Token:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJidWRpQHNpbWFuamEuY29tIiwicm9sZSI6IlVTRVIiLCJ1c2VySWQiOjIsImlhdCI6MTcxODM1NjgwMCwiZXhwIjoxNzE4NDQzMjAwfQ.signature
```

#### **Role-Based Access Control (RBAC)**
| Role | Access Level | Capabilities |
|------|-------------|--------------|
| **USER** | Standard | CRUD own transactions/targets, view/edit own profile |
| **ADMIN** | Full | All USER capabilities + view all users + system management |

#### **Password Security**
- ✅ BCrypt hashing algorithm (strength: 10 rounds)
- ✅ Salt auto-generated per user
- ✅ No plain text storage
- ✅ Password validation on change (old password must match)

**Example Code:**
```java
// UserService.java
public void changePassword(Long userId, ChangePasswordRequest request) {
    User user = findById(userId);
    
    // Validate old password with BCrypt
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
        throw new ValidationException("Password lama tidak cocok");
    }
    
    // Hash new password
    String hashedPassword = passwordEncoder.encode(request.getNewPassword());
    user.setPassword(hashedPassword);
    userRepository.save(user);
}
```

---

### **2. API Security**

#### **Endpoint Protection**

| Access Level | Pattern | Authentication |
|-------------|---------|----------------|
| **Public** | `/api/auth/**` | ❌ No token required |
| **Protected** | `/api/users/me`, `/api/transaksi/**`, `/api/target/**` | ✅ JWT required |
| **Admin Only** | `/api/users` (list), `/api/users/{id}` | ✅ JWT + ADMIN role |

**SecurityConfig.java:**
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/users").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

#### **CORS Configuration**
Mengizinkan frontend JavaFX untuk mengakses API.

```java
// WebConfig.java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
}
```

---

### **3. Ownership Validation**

Setiap user hanya dapat mengakses data miliknya sendiri.

**Example:**
```java
// TransaksiService.java
public List<Transaksi> getTransaksiByUser(Long userId) {
    // Only returns transactions belonging to this user
    return transaksiRepository.findByUserIdOrderByTanggalDesc(userId);
}

// TargetService.java
public Target getTargetById(Long targetId, Long userId) {
    Target target = targetRepository.findById(targetId)
        .orElseThrow(() -> new ResourceNotFoundException("Target tidak ditemukan"));
    
    // Verify ownership
    if (!target.getUser().getId().equals(userId)) {
        throw new ForbiddenException("Anda tidak memiliki akses ke target ini");
    }
    
    return target;
}
```

---

### **4. JWT Filter Chain**

Setiap request protected endpoint melalui filter:

1. **Extract token** dari header `Authorization: Bearer <token>`
2. **Validate token** (signature, expiration)
3. **Extract user info** (userId, email, role)
4. **Load user details** dari database
5. **Set authentication** ke SecurityContext
6. **Continue** ke controller

**JwtAuthFilter.java:**
```java
@Override
protected void doFilterInternal(HttpServletRequest request, 
                                HttpServletResponse response, 
                                FilterChain filterChain) {
    String token = extractToken(request);
    
    if (token != null && jwtUtil.validateToken(token)) {
        String email = jwtUtil.getEmailFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    filterChain.doFilter(request, response);
}
```

---

### **5. Error Security**

Sensitive information tidak di-expose ke client.

**GlobalExceptionHandler.java:**
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
    // Log internal error (with stack trace)
    log.error("Internal server error", ex);
    
    // Return sanitized error to client
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("Terjadi kesalahan sistem"));
}
```

---

## ✅ Validasi

### **1. Bean Validation (Input Validation)**

Menggunakan Jakarta Bean Validation annotations.

#### **Common Validation Annotations**

| Annotation | Fungsi | Example |
|-----------|--------|---------|
| `@NotBlank` | Field tidak boleh kosong | `nama`, `email`, `password` |
| `@Email` | Harus format email valid | `email` |
| `@Size(min, max)` | Panjang string | `@Size(min=6)` untuk password |
| `@Positive` | Harus angka positif | `jumlah`, `targetAmount` |
| `@NotNull` | Field tidak boleh null | `jenis`, `kategori` |
| `@Pattern` | Regex validation | Phone number format |

#### **Example: RegisterRequest.java**
```java
public class RegisterRequest {
    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;
    
    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;
    
    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;
    
    @NotBlank(message = "Konfirmasi password tidak boleh kosong")
    private String konfirmPassword;
}
```

#### **Controller Usage:**
```java
@PostMapping("/register")
public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    // @Valid triggers bean validation
    return ResponseEntity.ok(authService.register(request));
}
```

**Validation Error Response:**
```json
{
  "timestamp": "2026-06-14T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email tidak boleh kosong"
}
```

---

### **2. Business Validation (Service Layer)**

Validasi logic bisnis yang tidak bisa dilakukan di level bean validation.

#### **A. Password Confirmation Match**
```java
// AuthService.java
public AuthResponse register(RegisterRequest request) {
    // Validate password match
    if (!request.getPassword().equals(request.getKonfirmPassword())) {
        throw new ValidationException("Password dan konfirmasi tidak cocok");
    }
    ...
}
```

#### **B. Email Uniqueness**
```java
// AuthService.java
public AuthResponse register(RegisterRequest request) {
    // Check email already exists
    if (userRepository.existsByEmail(request.getEmail())) {
        throw new ValidationException("Email sudah terdaftar");
    }
    ...
}
```

#### **C. Date Constraints**
```java
// TransaksiService.java
public Transaksi createTransaksi(TransaksiRequest request, Long userId) {
    // Validate date not in future
    if (request.getTanggal().isAfter(LocalDate.now())) {
        throw new ValidationException("Tanggal transaksi tidak boleh di masa depan");
    }
    ...
}
```

#### **D. Password Change Validation**
```java
// UserService.java
public void changePassword(Long userId, ChangePasswordRequest request) {
    User user = findById(userId);
    
    // 1. Old password must match
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
        throw new ValidationException("Password lama tidak cocok");
    }
    
    // 2. New password must differ from old
    if (request.getOldPassword().equals(request.getNewPassword())) {
        throw new ValidationException("Password baru harus berbeda dari password lama");
    }
    
    // 3. New password must match confirmation
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
        throw new ValidationException("Password baru dan konfirmasi tidak cocok");
    }
    
    // 4. Password length validation (via bean validation)
    // @Size(min=6) in ChangePasswordRequest
    
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
}
```

#### **E. Target Deadline Validation**
```java
// TargetService.java
public Target createTarget(TargetRequest request, Long userId) {
    // Deadline must be in future
    if (request.getDeadline().isBefore(LocalDate.now())) {
        throw new ValidationException("Deadline harus di masa depan");
    }
    ...
}
```

#### **F. Positive Amount Validation**
```java
// TransaksiRequest.java
public class TransaksiRequest {
    @Positive(message = "Jumlah harus lebih dari 0")
    private Double jumlah;
}

// IsiCelenganRequest.java
public class IsiCelenganRequest {
    @NotNull(message = "Amount tidak boleh kosong")
    @Positive(message = "Amount harus lebih dari 0")
    private Double amount;
}
```

---

### **3. Exception Handling**

Semua validation errors ditangani oleh `GlobalExceptionHandler`.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle bean validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldError()
            .getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }
    
    // Handle business validation errors
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }
    
    // Handle resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

---

### **4. Validation Summary**

| Layer | Type | Implementation | Example |
|-------|------|----------------|---------|
| **DTO** | Bean Validation | `@Valid`, `@NotBlank`, `@Email`, `@Size`, `@Positive` | `RegisterRequest`, `TransaksiRequest` |
| **Service** | Business Logic | Custom validation methods | Password match, email uniqueness, date constraints |
| **Repository** | Database Constraints | `@Column(unique=true)`, `@Column(nullable=false)` | Email uniqueness at DB level |
| **Controller** | Error Handling | `@RestControllerAdvice` | Convert exceptions to HTTP responses |

---

## 🏗 Arsitektur

### **Arsitektur Layered (MVC + Service + Repository)**

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                         │
│                   (JavaFX Desktop App)                      │
└─────────────────────────────────────────────────────────────┘
                             │
                             │ HTTP/REST
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                         │
│        (AuthController, UserController, dll)                │
│  • Receive HTTP requests                                    │
│  • Input validation (@Valid)                                │
│  • Call service methods                                     │
│  • Return HTTP responses                                    │
└─────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                          │
│      (AuthService, UserService, TransaksiService)           │
│  • Business logic validation                                │
│  • Transaction management                                   │
│  • Data transformation (Entity ↔ DTO)                       │
│  • Call repository methods                                  │
└─────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER                         │
│    (UserRepository, TransaksiRepository, TargetRepo)        │
│  • Data access operations (CRUD)                            │
│  • JPQL/Query methods                                       │
│  • Extends JpaRepository                                    │
└─────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                      MODEL/ENTITY LAYER                     │
│              (User, Transaksi, Target)                      │
│  • Database table mapping (@Entity)                         │
│  • Relationships (@ManyToOne, @OneToMany)                   │
│  • Constraints (@Column, @NotNull)                          │
└─────────────────────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATABASE LAYER                        │
│                      (H2 In-Memory DB)                      │
│  Tables: USERS, TRANSAKSI, TARGET                           │
└─────────────────────────────────────────────────────────────┘
```

---

### **Security Flow (JWT Authentication)**

```
┌─────────┐                                    ┌─────────┐
│ Client  │                                    │ Backend │
└─────────┘                                    └─────────┘
     │                                              │
     │  POST /api/auth/login                       │
     │  { email, password }                        │
     │─────────────────────────────────────────────>│
     │                                              │
     │                                    ┌─────────▼──────────┐
     │                                    │  AuthController    │
     │                                    │  @Valid validate   │
     │                                    └─────────┬──────────┘
     │                                              │
     │                                    ┌─────────▼──────────┐
     │                                    │   AuthService      │
     │                                    │  • Find user       │
     │                                    │  • Check BCrypt    │
     │                                    │  • Generate JWT    │
     │                                    └─────────┬──────────┘
     │                                              │
     │  { token, userId, nama, role }              │
     │<─────────────────────────────────────────────│
     │                                              │
     │  GET /api/users/me                          │
     │  Header: Authorization: Bearer <token>      │
     │─────────────────────────────────────────────>│
     │                                              │
     │                                    ┌─────────▼──────────┐
     │                                    │  JwtAuthFilter     │
     │                                    │  • Extract token   │
     │                                    │  • Validate token  │
     │                                    │  • Load user       │
     │                                    │  • Set auth        │
     │                                    └─────────┬──────────┘
     │                                              │
     │                                    ┌─────────▼──────────┐
     │                                    │  UserController    │
     │                                    │  • Get current user│
     │                                    └─────────┬──────────┘
     │                                              │
     │  { id, nama, email, ... }                   │
     │<─────────────────────────────────────────────│
     │                                              │
```

---

## 📂 Struktur Proyek

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/simanja/backend/
│   │   │   ├── SimanjaBackendApplication.java    ← Entry point
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java          ← Seed demo data
│   │   │   │   └── WebConfig.java                ← CORS config
│   │   │   │
│   │   │   ├── controller/                       ← REST Controllers (MVC)
│   │   │   │   ├── AuthController.java           ← Login, register
│   │   │   │   ├── UserController.java           ← User management
│   │   │   │   ├── TransaksiController.java      ← Transaction CRUD
│   │   │   │   └── TargetController.java         ← Savings target CRUD
│   │   │   │
│   │   │   ├── service/                          ← Business Logic Layer
│   │   │   │   ├── AuthService.java              ← Auth logic
│   │   │   │   ├── UserService.java              ← User operations
│   │   │   │   ├── TransaksiService.java         ← Transaction logic
│   │   │   │   └── TargetService.java            ← Target logic
│   │   │   │
│   │   │   ├── repository/                       ← Data Access Layer
│   │   │   │   ├── UserRepository.java           ← User queries (JPA)
│   │   │   │   ├── TransaksiRepository.java      ← Transaction queries
│   │   │   │   └── TargetRepository.java         ← Target queries
│   │   │   │
│   │   │   ├── model/                            ← Entity/Domain Models
│   │   │   │   ├── Kategorisasi.java             ← Interface (Abstraction)
│   │   │   │   ├── BaseEntity.java               ← Abstract class (Inheritance)
│   │   │   │   ├── User.java                     ← User entity
│   │   │   │   ├── Transaksi.java                ← Transaction entity
│   │   │   │   └── Target.java                   ← Target entity
│   │   │   │
│   │   │   ├── dto/                              ← Data Transfer Objects
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── ChangePasswordRequest.java    ← NEW ⭐
│   │   │   │   │   ├── TransaksiRequest.java
│   │   │   │   │   ├── TargetRequest.java
│   │   │   │   │   ├── UpdateUserRequest.java
│   │   │   │   │   └── IsiCelenganRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── TransaksiResponse.java
│   │   │   │       ├── TargetResponse.java
│   │   │   │       ├── SummaryResponse.java
│   │   │   │       └── LaporanResponse.java
│   │   │   │
│   │   │   ├── security/                         ← Security Config
│   │   │   │   ├── SecurityConfig.java           ← Spring Security config
│   │   │   │   ├── JwtUtil.java                  ← JWT generate/validate
│   │   │   │   ├── JwtAuthFilter.java            ← JWT filter chain
│   │   │   │   └── UserDetailsServiceImpl.java   ← Load user for auth
│   │   │   │
│   │   │   └── exception/                        ← Exception Handling
│   │   │       ├── GlobalExceptionHandler.java   ← @RestControllerAdvice
│   │   │       ├── ResourceNotFoundException.java
│   │   │       └── ValidationException.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties            ← Config (DB, JPA, JWT)
│   │       └── application-dev.properties        ← Dev config alternative
│   │
│   └── test/
│       └── java/com/simanja/backend/
│           └── SimanjaBackendApplicationTests.java
│
├── pom.xml                                      ← Maven dependencies
├── README.md                                    ← This file
├── QUICK_START.bat                              ← Quick start script
├── FIX_DATABASE.bat                             ← Fix DB issues script
└── CHECK_PROJECT.bat                            ← Check project health
```

---

## 🎓 Pemenuhan Kriteria PBO

### **1. ✅ Arsitektur MVC + Service + Repository**

| Layer | Komponen | Fungsi |
|-------|----------|--------|
| **Model** | `User.java`, `Transaksi.java`, `Target.java` | Entity/domain objects |
| **View** | JavaFX Frontend (separate project) | User interface |
| **Controller** | `AuthController`, `UserController`, dll | Handle HTTP requests |
| **Service** | `AuthService`, `UserService`, dll | Business logic |
| **Repository** | `UserRepository`, `TransaksiRepository`, dll | Data access |

**Example Code:**
```java
// Controller → Service → Repository flow

// 1. Controller (MVC)
@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {
    @Autowired
    private TransaksiService transaksiService;
    
    @PostMapping
    public ResponseEntity<TransaksiResponse> create(
            @Valid @RequestBody TransaksiRequest request) {
        return ResponseEntity.ok(transaksiService.createTransaksi(request, userId));
    }
}

// 2. Service (Business Logic)
@Service
public class TransaksiService {
    @Autowired
    private TransaksiRepository transaksiRepository;
    
    public TransaksiResponse createTransaksi(TransaksiRequest request, Long userId) {
        // Business validation
        if (request.getTanggal().isAfter(LocalDate.now())) {
            throw new ValidationException("Tanggal tidak boleh masa depan");
        }
        
        // Call repository
        Transaksi transaksi = transaksiRepository.save(newTransaksi);
        return toResponse(transaksi);
    }
}

// 3. Repository (Data Access)
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    List<Transaksi> findByUserIdOrderByTanggalDesc(Long userId);
}
```

---

### **2. ✅ H2 Database**

**Configuration (`application.properties`):**
```properties
# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:simanja_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console (http://localhost:8080/h2-console)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Database Schema:**
- `USERS` - User accounts (id, nama, email, password, role)
- `TRANSAKSI` - Financial transactions (id, judul, jumlah, jenis, kategori, tanggal, user_id)
- `TARGET` - Savings targets (id, nama, target_amount, current_amount, deadline, user_id)

**Lifecycle:** In-memory → Data hilang saat app stop → Re-seed saat start (via `DataInitializer`)

---

### **3. ✅ JPA/Hibernate (ORM)**

**Entity Mapping:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaksi> transaksiList;
}
```

**JPA Relationships:**
- `User` ←1:N→ `Transaksi` (One user has many transactions)
- `User` ←1:N→ `Target` (One user has many savings targets)

**JPA Queries:**
```java
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    // JPQL custom query
    @Query("SELECT t FROM Transaksi t WHERE t.user.id = :userId 
            AND (LOWER(t.judul) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            OR LOWER(t.kategori) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Transaksi> searchByKeyword(@Param("userId") Long userId, 
                                     @Param("keyword") String keyword);
    
    // Derived query method
    List<Transaksi> findByUserIdOrderByTanggalDesc(Long userId);
}
```

**Hibernate Configuration:**
```properties
# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

---

### **4. ✅ Validasi**

**A. Bean Validation:**
```java
public class TransaksiRequest {
    @NotBlank(message = "Judul tidak boleh kosong")
    private String judul;
    
    @NotNull(message = "Jumlah tidak boleh kosong")
    @Positive(message = "Jumlah harus lebih dari 0")
    private Double jumlah;
    
    @NotNull(message = "Tanggal tidak boleh kosong")
    private LocalDate tanggal;
}
```

**B. Business Validation:**
```java
@Service
public class TransaksiService {
    public TransaksiResponse createTransaksi(TransaksiRequest request, Long userId) {
        // Date validation
        if (request.getTanggal().isAfter(LocalDate.now())) {
            throw new ValidationException("Tanggal tidak boleh di masa depan");
        }
        
        // Amount validation
        if (request.getJumlah() <= 0) {
            throw new ValidationException("Jumlah harus lebih dari 0");
        }
        
        ...
    }
}
```

---

### **5. ✅ Spring Security + JWT**

**A. Authentication:**
```java
@Service
public class AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ValidationException("Email/password salah"));
        
        // Validate password with BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationException("Email/password salah");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user);
        
        return new AuthResponse(token, user);
    }
}
```

**B. Authorization:**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()          // Public
                .requestMatchers("/api/users").hasRole("ADMIN")       // Admin only
                .anyRequest().authenticated()                         // Protected
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();    // BCrypt hashing
    }
}
```

---

### **6. ✅ 4 Pilar OOP**

#### **A. Encapsulation**
Semua entity/model menggunakan private fields + getter/setter.

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // Private field
    
    private String nama;              // Private field
    
    @Column(unique = true)
    private String email;             // Private field
    
    // Public getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
}
```

---

#### **B. Inheritance**
`BaseEntity` (abstract class) diwarisi oleh `Target`.

```java
// Parent abstract class
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters/setters
}

// Child class inherits from BaseEntity
@Entity
public class Target extends BaseEntity {
    private String nama;
    private Double targetAmount;
    
    // Inherits: id, createdAt, updatedAt, onCreate(), onUpdate()
}
```

**Benefit:** Semua entity yang extends `BaseEntity` otomatis punya `id`, `createdAt`, `updatedAt` dan auto-timestamp.

---

#### **C. Polymorphism**
Method overriding & lambda expressions.

**1. Method Overriding:**
```java
// Interface
public interface Kategorisasi {
    String getDisplayName();
    String getJenisLabel();
}

// Transaksi implements interface
@Entity
public class Transaksi implements Kategorisasi {
    private String jenis;  // "PEMASUKAN" or "PENGELUARAN"
    
    @Override
    public String getDisplayName() {
        return judul + " - " + kategori;
    }
    
    @Override
    public String getJenisLabel() {
        return jenis.equals("PEMASUKAN") ? "Pemasukan" : "Pengeluaran";
    }
}

// Usage (polymorphic behavior)
List<Kategorisasi> items = new ArrayList<>();
items.add(new Transaksi(...));
items.add(new Target(...));

for (Kategorisasi item : items) {
    System.out.println(item.getDisplayName());  // Calls appropriate override
}
```

**2. Lambda Expressions (Functional Polymorphism):**
```java
// TransaksiService.java
public SummaryResponse getSummary(Long userId) {
    List<Transaksi> transaksiList = transaksiRepository.findByUserId(userId);
    
    // Lambda: Polymorphic filter
    double totalPemasukan = transaksiList.stream()
        .filter(t -> t.getJenis().equals("PEMASUKAN"))  // Lambda predicate
        .mapToDouble(Transaksi::getJumlah)              // Method reference
        .sum();
    
    double totalPengeluaran = transaksiList.stream()
        .filter(t -> t.getJenis().equals("PENGELUARAN"))
        .mapToDouble(Transaksi::getJumlah)
        .sum();
    
    return new SummaryResponse(totalPemasukan, totalPengeluaran, ...);
}
```

---

#### **D. Abstraction**
Interface & Abstract Class untuk hide implementation details.

**1. Interface Abstraction:**
```java
// Abstract contract (interface)
public interface Kategorisasi {
    String getDisplayName();      // Abstract method (no body)
    String getJenisLabel();       // Abstract method (no body)
}

// Concrete implementation
public class Transaksi implements Kategorisasi {
    @Override
    public String getDisplayName() {
        return judul + " - " + kategori;    // Implementation
    }
    
    @Override
    public String getJenisLabel() {
        return jenis.equals("PEMASUKAN") ? "Pemasukan" : "Pengeluaran";
    }
}
```

**2. Abstract Class:**
```java
@MappedSuperclass
public abstract class BaseEntity {
    protected Long id;
    protected LocalDateTime createdAt;
    
    @PrePersist
    protected abstract void onCreate();    // Abstract method
    
    // Concrete methods
    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
```

**3. Repository Abstraction (JpaRepository):**
```java
// Spring Data JPA provides abstraction
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);    // Abstract declaration
    // Implementation auto-generated by Spring Data JPA
}

// Usage (no need to know SQL implementation)
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
    }
}
```

---

### **Ringkasan Pemenuhan Kriteria**

| # | Kriteria | Status | Evidence |
|---|----------|--------|----------|
| 1 | MVC + Service + Repository | ✅ | `controller/` → `service/` → `repository/` |
| 2 | H2 Database | ✅ | `jdbc:h2:mem:simanja_db` (in-memory) |
| 3 | JPA/Hibernate | ✅ | `@Entity`, `@ManyToOne`, JpaRepository, JPQL |
| 4 | Validation | ✅ | Bean Validation (`@Valid`, `@NotBlank`) + Business Logic |
| 5 | Security | ✅ | Spring Security + JWT + BCrypt + RBAC |
| 6 | Encapsulation | ✅ | Private fields + getter/setter di semua Entity |
| 7 | Inheritance | ✅ | `BaseEntity` (abstract) ← `Target` extends |
| 8 | Polymorphism | ✅ | Override `getDisplayName()`, lambda `.filter()`, `.map()` |
| 9 | Abstraction | ✅ | Interface `Kategorisasi`, abstract `BaseEntity`, JpaRepository |

---

## ✅ Testing

### **1. Test Login API**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"budi@simanja.com\",\"password\":\"budi123\"}"
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJidWRpQHNpbWFuamEuY29tIiwicm9sZSI6IlVTRVIiLCJ1c2VySWQiOjIsImlhdCI6MTcxODM1NjgwMCwiZXhwIjoxNzE4NDQzMjAwfQ.xxxxxx",
  "tokenType": "Bearer",
  "userId": 2,
  "nama": "Budi Santoso",
  "email": "budi@simanja.com",
  "role": "USER"
}
```

---

### **2. Test Register API**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"nama\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"password123\",\"konfirmPassword\":\"password123\"}"
```

---

### **3. Test Get Profile (Protected)**
```bash
# Save token dari login response
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "id": 2,
  "nama": "Budi Santoso",
  "email": "budi@simanja.com",
  "username": "budisantoso",
  "telepon": "0812-3456-7890",
  "jenisKelamin": "Laki-laki",
  "tanggalLahir": "15 Juni 1995",
  "alamat": "Jl. Midnight Ledger No. 88, Jakarta Selatan",
  "role": "USER"
}
```

---

### **4. Test Change Password**
```bash
curl -X PUT http://localhost:8080/api/users/change-password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"oldPassword\":\"budi123\",\"newPassword\":\"budibaru123\",\"confirmPassword\":\"budibaru123\"}"
```

**Expected Response:**
```json
{
  "message": "Password berhasil diubah"
}
```

---

### **5. Test Create Transaction**
```bash
curl -X POST http://localhost:8080/api/transaksi \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"judul\":\"Gaji Bulanan\",\"jumlah\":5000000,\"jenis\":\"PEMASUKAN\",\"kategori\":\"Gaji\",\"tanggal\":\"2026-06-01\",\"keterangan\":\"Gaji bulan Juni\"}"
```

---

### **6. Test Get Summary**
```bash
curl -X GET http://localhost:8080/api/transaksi/summary \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "totalPemasukan": 14000000.0,
  "totalPengeluaran": 3139000.0,
  "saldo": 10861000.0,
  "totalTabungan": 122000000.0,
  "jumlahTransaksi": 9,
  "jumlahTarget": 4
}
```

---

### **7. Test Create Target**
```bash
curl -X POST http://localhost:8080/api/target \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"nama\":\"Liburan Bali\",\"iconEmoji\":\"🏖️\",\"targetAmount\":10000000,\"deadline\":\"2026-12-31\"}"
```

---

### **8. Test Deposit to Target**
```bash
curl -X POST http://localhost:8080/api/target/5/isi \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"amount\":500000}"
```

---

### **9. Test Admin Get All Users (Admin Only)**
```bash
# Login as admin first
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"admin@simanja.com\",\"password\":\"admin123\"}"

# Use admin token
ADMIN_TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

### **10. Test Validation Error**
```bash
# Test dengan password mismatch
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"nama\":\"Test\",\"email\":\"test@test.com\",\"password\":\"pass123\",\"konfirmPassword\":\"different\"}"
```

**Expected Error Response:**
```json
{
  "timestamp": "2026-06-14T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Password dan konfirmasi tidak cocok"
}
```

---

## 🐛 Troubleshooting

| File | Deskripsi |
|------|-----------|
| `pom.xml` | Maven dependencies & project configuration |
| `application.properties` | Database, JPA, JWT, logging configuration |
| `SimanjaBackendApplication.java` | Entry point Spring Boot application |
| `DataInitializer.java` | Seed data demo (3 users, 9 transaksi, 4 target) |
| `SecurityConfig.java` | Spring Security & JWT configuration |
| `JwtUtil.java` | JWT token generation & validation |
| `GlobalExceptionHandler.java` | Centralized error handling |
| `QUICK_START.bat` | Script untuk start aplikasi dengan cepat |
| `FIX_DATABASE.bat` | Script untuk fix database issues |

---

## 📊 Statistik API

| Kategori | Jumlah |
|----------|--------|
| **Total Endpoints** | 21 |
| Public Endpoints | 2 (login, register) |
| Protected Endpoints | 19 (requires JWT) |
| Admin-Only Endpoints | 2 (get all users, get user by ID) |
| **Models/Entities** | 5 (User, Transaksi, Target, BaseEntity, Kategorisasi) |
| **Services** | 4 (AuthService, UserService, TransaksiService, TargetService) |
| **Repositories** | 3 (UserRepository, TransaksiRepository, TargetRepository) |
| **Controllers** | 4 (AuthController, UserController, TransaksiController, TargetController) |
| **DTOs** | 12 (6 request, 6 response) |

---

## ✅ Testing

### **1. Test Login API**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"budi@simanja.com\",\"password\":\"budi123\"}"
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "userId": 2,
  "nama": "Budi Santoso",
  "email": "budi@simanja.com",
  "role": "USER"
}
```

### **2. Test Protected Endpoint**
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <YOUR_TOKEN>"
```

### **3. Test Change Password**
```bash
curl -X PUT http://localhost:8080/api/users/change-password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -d "{\"oldPassword\":\"budi123\",\"newPassword\":\"budibaru123\",\"confirmPassword\":\"budibaru123\"}"
```

---

## 🐛 Troubleshooting

### **Problem 1: Port 8080 sudah digunakan**

**Error Message:**
```
***************************
APPLICATION FAILED TO START
***************************

Description:
Web server failed to start. Port 8080 was already in use.
```

**Solution A:** Ganti port di `application.properties`
```properties
server.port=8081
```

**Solution B:** Kill process yang menggunakan port 8080
```bash
# Windows CMD
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F

# Windows PowerShell
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force
```

---

### **Problem 2: Maven not found**

**Error Message:**
```
'mvn' is not recognized as an internal or external command
```

**Solution:**
1. Download Maven: https://maven.apache.org/download.cgi
2. Extract ke `C:\Program Files\Apache\maven`
3. Add to System PATH:
   - System Properties → Environment Variables
   - Edit `Path` → Add `C:\Program Files\Apache\maven\bin`
4. Restart terminal
5. Verify: `mvn -version`

---

### **Problem 3: Java version mismatch**

**Error Message:**
```
Invalid target release: 17
```

**Solution:**
1. Check Java version: `java -version`
2. Must be Java 17 or higher
3. Download Java 17: https://adoptium.net/
4. Set JAVA_HOME environment variable
5. Verify: `java -version` dan `javac -version`

---

### **Problem 4: Database connection failed**

**Error Message:**
```
Caused by: org.h2.jdbc.JdbcSQLException: Database may be already in use
```

**Solution:** Run fix database script
```bash
cd backend
FIX_DATABASE.bat
```

**Script akan:**
1. Clean Maven project
2. Delete old H2 data files (jika ada)
3. Clear Maven cache untuk H2
4. Download fresh dependencies
5. Rebuild & start application

---

### **Problem 5: Compilation error setelah git pull**

**Error Message:**
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin
```

**Solution:**
```bash
# Clean & force update dependencies
mvn clean install -U

# Atau rebuild dari scratch
mvn clean compile
mvn spring-boot:run
```

---

### **Problem 6: JWT token expired**

**Error Response:**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token has expired"
}
```

**Solution:**
1. Login ulang untuk mendapatkan token baru
2. Token valid selama 24 jam
3. Frontend akan auto-redirect ke login page

---

### **Problem 7: Cannot access H2 Console**

**Problem:** `http://localhost:8080/h2-console` tidak bisa diakses

**Solution:**
1. Verify H2 console enabled di `application.properties`:
   ```properties
   spring.h2.console.enabled=true
   ```
2. Check correct JDBC URL: `jdbc:h2:mem:simanja_db`
3. Username: `sa`, Password: *(kosong)*

---

### **Problem 8: Data hilang setelah restart**

**Expected Behavior:** Database adalah **in-memory**, data akan hilang saat aplikasi stop.

**Solution:**
- Data demo akan otomatis di-seed ulang saat aplikasi start
- Lihat `DataInitializer.java` untuk data yang di-load
- Untuk production, ganti ke file-based H2 atau database lain (MySQL/PostgreSQL)

---

### **Problem 9: CORS error dari frontend**

**Error in Browser Console:**
```
Access to XMLHttpRequest at 'http://localhost:8080/api/...' from origin 
'http://localhost:3000' has been blocked by CORS policy
```

**Solution:**
Verify CORS configuration di `WebConfig.java`:
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
}
```

---

### **Problem 10: Password validation error**

**Error:**
```json
{
  "message": "Password lama tidak cocok"
}
```

**Solution:**
1. Pastikan `oldPassword` sesuai dengan password saat ini
2. Password case-sensitive
3. Cek tidak ada spasi extra di awal/akhir password

---

### **Problem 11: Dependency download gagal**

**Error:**
```
Could not resolve dependencies for project com.simanja:backend
```

**Solution:**
```bash
# Clear Maven cache & redownload
mvn dependency:purge-local-repository
mvn clean install

# Atau force update
mvn clean install -U
```

---

### **Problem 12: Application context failed to start**

**Error:**
```
***************************
APPLICATION FAILED TO START
***************************
```

**Solution:**
1. Check full error message di console
2. Common causes:
   - Port conflict → Ganti port atau kill process
   - Dependency missing → Run `mvn clean install`
   - Configuration error → Check `application.properties`
3. Run `CHECK_PROJECT.bat` untuk diagnose

---

### **Problem 13: IDE tidak detect Spring Boot**

**IntelliJ IDEA:**
1. `File` → `Invalidate Caches` → Restart
2. `File` → `Project Structure` → SDK harus Java 17
3. Right-click `pom.xml` → `Maven` → `Reload Project`

**Eclipse:**
1. Right-click project → `Maven` → `Update Project`
2. Check Java Compiler Level: Java 17
3. Clean project: `Project` → `Clean`

---

### **Need Help?**

Jika masih ada masalah:
1. Check full error log di console
2. Verify semua prerequisites terinstall (Java 17, Maven 3.6+)
3. Run `CHECK_PROJECT.bat` untuk diagnostic
4. Check file `application.properties` untuk configuration errors

---

## 📁 File Penting

| File | Deskripsi |
|------|-----------|
| `pom.xml` | Maven dependencies & project configuration |
| `application.properties` | Database, JPA, JWT, logging configuration |
| `SimanjaBackendApplication.java` | Entry point Spring Boot application |
| `DataInitializer.java` | Seed data demo (3 users, 9 transaksi, 4 target) |
| `SecurityConfig.java` | Spring Security & JWT configuration |
| `JwtUtil.java` | JWT token generation & validation |
| `GlobalExceptionHandler.java` | Centralized error handling |
| `QUICK_START.bat` | Script untuk start aplikasi dengan cepat |
| `FIX_DATABASE.bat` | Script untuk fix database issues |
| `CHECK_PROJECT.bat` | Script untuk check project health |

---

## 📊 Statistik API

| Kategori | Jumlah |
|----------|--------|
| **Total Endpoints** | 23 |
| Public Endpoints | 2 (login, register) |
| Protected Endpoints | 19 (requires JWT) |
| Admin-Only Endpoints | 2 (get all users, get user by ID) |
| **Models/Entities** | 5 (User, Transaksi, Target, BaseEntity, Kategorisasi) |
| **Services** | 4 (AuthService, UserService, TransaksiService, TargetService) |
| **Repositories** | 3 (UserRepository, TransaksiRepository, TargetRepository) |
| **Controllers** | 4 (AuthController, UserController, TransaksiController, TargetController) |
| **DTOs** | 12 (6 request, 6 response) |
| **Security Components** | 4 (SecurityConfig, JwtUtil, JwtAuthFilter, UserDetailsServiceImpl) |
| **Exception Handlers** | 3 (Global, ResourceNotFound, Validation) |

---

## 📝 Changelog

### **Version 1.0.0** (14 Juni 2026)

#### **Features**
- ✅ JWT authentication & authorization (stateless)
- ✅ Role-based access control (ADMIN/USER)
- ✅ CRUD endpoints untuk User, Transaksi, Target
- ✅ **NEW:** Change password endpoint dengan validasi lengkap
- ✅ Search transaksi by keyword
- ✅ Summary keuangan (saldo, pemasukan, pengeluaran)
- ✅ Laporan per bulan
- ✅ Target tabungan dengan progress tracking
- ✅ Auto-achievement detection untuk target

#### **Security**
- ✅ BCrypt password hashing
- ✅ JWT token dengan expiration (24 jam)
- ✅ Ownership validation (user hanya akses data sendiri)
- ✅ CORS configuration untuk JavaFX frontend
- ✅ Global exception handling dengan sanitized errors

#### **Validation**
- ✅ Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Size`, `@Positive`)
- ✅ Business validation di Service layer
- ✅ Password confirmation match
- ✅ Email uniqueness check
- ✅ Date constraints (tidak boleh masa depan)
- ✅ Old password validation pada change password

#### **Database**
- ✅ H2 in-memory database (dev/testing)
- ✅ JPA/Hibernate ORM dengan relationships (1:N)
- ✅ Auto-schema generation (`create-drop`)
- ✅ Data seeder dengan 3 demo accounts
- ✅ H2 Console untuk debugging

#### **Architecture**
- ✅ MVC + Service + Repository pattern
- ✅ Clear separation of concerns
- ✅ DTO pattern untuk API request/response
- ✅ 4 OOP pillars (Encapsulation, Inheritance, Polymorphism, Abstraction)

#### **Developer Tools**
- ✅ `QUICK_START.bat` - Quick start script
- ✅ `FIX_DATABASE.bat` - Fix database issues
- ✅ `CHECK_PROJECT.bat` - Project health check
- ✅ Comprehensive API documentation
- ✅ curl test examples

---

**Dibuat oleh:** Team SiManja  
**Project:** UAS Lab Pemrograman Berorientasi Objek  
**Tech Stack:** Spring Boot 3.2.5 + Java 17 + H2 Database  
**Status:** ✅ Production Ready  
**License:** Academic Project

---

<div align="center">

**🎓 Project UAS Lab PBO - Universitas**

*Sistem Manajemen Keuangan (SiManja)*

Made with ❤️ by Team SiManja

</div>
