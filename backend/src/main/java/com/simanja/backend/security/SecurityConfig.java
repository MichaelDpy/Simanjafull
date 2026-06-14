package com.simanja.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig — Konfigurasi Spring Security.
 *
 * Implementasi Security (autentikasi + otorisasi):
 * - Stateless authentication menggunakan JWT
 * - Role-based authorization: ADMIN vs USER
 * - Endpoint publik: /api/auth/** (login & register)
 * - H2 Console diakses tanpa autentikasi (untuk keperluan demo)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter      = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * BCrypt password encoder — untuk hashing password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationProvider menggunakan DAO + BCrypt.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager — dibutuhkan oleh AuthController.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Security Filter Chain — konfigurasi utama keamanan aplikasi.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Nonaktifkan CSRF (karena stateless JWT)
            .csrf(AbstractHttpConfigurer::disable)

            // Konfigurasi otorisasi endpoint
            .authorizeHttpRequests(auth -> auth
                // Endpoint publik — tidak perlu autentikasi
                .requestMatchers("/api/auth/**").permitAll()

                // H2 Console — untuk debugging / demo
                .requestMatchers("/h2-console/**").permitAll()

                // Endpoint khusus ADMIN
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")

                // Semua endpoint API lainnya — butuh autentikasi
                .anyRequest().authenticated()
            )

            // Stateless session (tidak menyimpan session di server)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Izinkan H2 Console di iframe
            .headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin())
            )

            // Pasang authentication provider
            .authenticationProvider(authenticationProvider())

            // Pasang JWT filter sebelum UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
