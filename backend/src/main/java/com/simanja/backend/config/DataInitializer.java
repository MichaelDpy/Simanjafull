package com.simanja.backend.config;

import com.simanja.backend.model.Target;
import com.simanja.backend.model.Transaksi;
import com.simanja.backend.model.Transaksi.JenisTransaksi;
import com.simanja.backend.model.User;
import com.simanja.backend.repository.TargetRepository;
import com.simanja.backend.repository.TransaksiRepository;
import com.simanja.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * DataInitializer — Seed data awal untuk H2 database.
 * Data identik dengan dummy data di frontend JavaFX.
 *
 * Akun demo:
 * - admin@simanja.com / admin12345 (ADMIN)
 * - budi@simanja.com  / budi12345  (USER)
 * - siti@simanja.com  / siti12345  (USER)
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(UserRepository userRepo,
                                      TransaksiRepository transaksiRepo,
                                      TargetRepository targetRepo,
                                      PasswordEncoder encoder) {
        return args -> {
            // Cek apakah data sudah ada (untuk file-based database)
            if (userRepo.count() > 0) {
                log.info("=== Data sudah ada, skip initialization ===");
                return;
            }
            
            log.info("=== Memuat data awal SiManja ===");

            // =============================================
            // SEED USERS
            // =============================================

            User admin = new User("Admin SiManja", "admin@simanja.com",
                    encoder.encode("admin12345"), "ADMIN");
            admin.setUsername("adminsimanja");
            admin.setTelepon("0812-0000-0001");
            admin.setJenisKelamin("Laki-laki");
            admin.setTanggalLahir("01 Januari 2000");
            admin.setAlamat("Jl. Simanja No. 1, Jakarta Pusat");
            admin = userRepo.save(admin);

            User budi = new User("Budi Santoso", "budi@simanja.com",
                    encoder.encode("budi12345"), "USER");
            budi.setUsername("budisantoso");
            budi.setTelepon("0812-3456-7890");
            budi.setJenisKelamin("Laki-laki");
            budi.setTanggalLahir("15 Juni 1995");
            budi.setAlamat("Jl. Midnight Ledger No. 88, Jakarta Selatan");
            budi = userRepo.save(budi);

            User siti = new User("Siti Rahayu", "siti@simanja.com",
                    encoder.encode("siti12345"), "USER");
            siti.setUsername("sitirahayu");
            siti.setTelepon("0856-7890-1234");
            siti.setJenisKelamin("Perempuan");
            siti.setTanggalLahir("20 Maret 1998");
            siti.setAlamat("Jl. Mawar No. 12, Bandung");
            siti = userRepo.save(siti);

            log.info("✓ 3 user berhasil dimuat");

            // =============================================
            // SEED TRANSAKSI (untuk user Budi)
            // =============================================

            transaksiRepo.save(new Transaksi("Gaji Bulanan",     8_500_000.0, JenisTransaksi.PEMASUKAN,
                    "Gaji",         LocalDate.of(2025, 6, 1),  "Gaji bulan Juni", budi));
            transaksiRepo.save(new Transaksi("Belanja Bulanan",  1_200_000.0, JenisTransaksi.PENGELUARAN,
                    "Kebutuhan",    LocalDate.of(2025, 6, 2),  "Supermarket", budi));
            transaksiRepo.save(new Transaksi("Bayar Listrik",      450_000.0, JenisTransaksi.PENGELUARAN,
                    "Tagihan",      LocalDate.of(2025, 6, 3),  "PLN", budi));
            transaksiRepo.save(new Transaksi("Freelance Web",    2_500_000.0, JenisTransaksi.PEMASUKAN,
                    "Freelance",    LocalDate.of(2025, 6, 5),  "Project klien A", budi));
            transaksiRepo.save(new Transaksi("Makan Siang",          85_000.0, JenisTransaksi.PENGELUARAN,
                    "Makanan",      LocalDate.of(2025, 6, 6),  "Resto XYZ", budi));
            transaksiRepo.save(new Transaksi("Transport",           350_000.0, JenisTransaksi.PENGELUARAN,
                    "Transportasi", LocalDate.of(2025, 6, 7),  "Bensin & parkir", budi));
            transaksiRepo.save(new Transaksi("Investasi Reksa",  1_000_000.0, JenisTransaksi.PENGELUARAN,
                    "Investasi",    LocalDate.of(2025, 6, 8),  "Bibit app", budi));
            transaksiRepo.save(new Transaksi("Bonus Proyek",     3_000_000.0, JenisTransaksi.PEMASUKAN,
                    "Bonus",        LocalDate.of(2025, 6, 10), "Klien B", budi));
            transaksiRepo.save(new Transaksi("Langganan Netflix",    54_000.0, JenisTransaksi.PENGELUARAN,
                    "Hiburan",      LocalDate.of(2025, 6, 11), "Netflix", budi));

            log.info("✓ 9 transaksi berhasil dimuat");

            // =============================================
            // SEED TARGET / CELENGAN (untuk user Budi)
            // =============================================

            Target t1 = new Target("Dana Liburan Jepang", "🏖️",
                    20_000_000.0, 15_000_000.0, LocalDate.of(2024, 12, 1), false, budi);
            targetRepo.save(t1);

            Target t2 = new Target("Dana Darurat", "🛡️",
                    50_000_000.0, 50_000_000.0, LocalDate.of(2024, 2, 1), true, budi);
            t2.setCompletedAt(LocalDate.of(2024, 2, 1));
            targetRepo.save(t2);

            Target t3 = new Target("Macbook Pro M3", "💻",
                    30_000_000.0, 12_000_000.0, LocalDate.of(2025, 3, 1), false, budi);
            targetRepo.save(t3);

            Target t4 = new Target("DP Rumah", "🏠",
                    150_000_000.0, 45_000_000.0, LocalDate.of(2027, 6, 1), false, budi);
            targetRepo.save(t4);

            log.info("✓ 4 target celengan berhasil dimuat");
            log.info("=== Data awal SiManja selesai dimuat ===");
            log.info("H2 Console: http://localhost:8080/h2-console");
            log.info("API Base  : http://localhost:8080/api");
        };
    }
}
