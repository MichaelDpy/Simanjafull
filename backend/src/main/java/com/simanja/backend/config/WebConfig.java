package com.simanja.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig — Konfigurasi CORS untuk mengizinkan JavaFX frontend mengakses API.
 * JavaFX mengakses backend melalui HTTP client lokal.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:*",    // JavaFX app
                        "http://127.0.0.1:*"     // Alternatif lokal
                )
                .allowedOriginPatterns("*")      // Untuk development
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
