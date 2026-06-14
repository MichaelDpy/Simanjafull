package com.simanja.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SimanjaBackendApplication — Entry point aplikasi Spring Boot SiManja.
 *
 * Cara menjalankan:
 *   mvn spring-boot:run
 *
 * Akun demo:
 *   admin@simanja.com / admin12345 (ADMIN)
 *   budi@simanja.com  / budi12345  (USER)
 *   siti@simanja.com  / siti12345  (USER)
 *
 * H2 Console : http://localhost:8080/h2-console
 * API Base   : http://localhost:8080/api
 */
@SpringBootApplication
public class SimanjaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimanjaBackendApplication.class, args);
    }
}
